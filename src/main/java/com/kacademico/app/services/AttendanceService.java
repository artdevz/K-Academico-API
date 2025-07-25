package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.attendance.AttendanceRequestDTO;
import com.kacademico.app.dto.attendance.AttendanceResponseDTO;
import com.kacademico.app.dto.attendance.AttendanceUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Attendance;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.repositories.IAttendanceRepository;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.domain.repositories.ILessonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    
    private final IAttendanceRepository attendanceR;
    private final IEnrolleeRepository enrolleeR;
    private final ILessonRepository lessonR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;
        
    @Async
    public CompletableFuture<String> createAsync(AttendanceRequestDTO data) {
        log.info("[API] Iniciando criação de presença para enrolleeId: {} e lessonId: {}", data.enrollee(), data.lesson());

        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findWithEvaluationsAndAttendancesById(data.enrollee()), "Enrollee not Found");
        Lesson lesson = finder.findByIdOrThrow(lessonR.findById(data.lesson()), "Lesson not Found");

        ensureAttendanceNotExists(enrollee, lesson);

        Attendance attendance = requestMapper.toAttendance(data);
        Attendance saved = attendanceR.save(attendance);
        updateAbsences(enrollee);

        log.info("[API] Presença criada com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Attendance successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<AttendanceResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as presenças");
        List<AttendanceResponseDTO> response = responseMapper.toResponseDTOList(attendanceR.findAll(), responseMapper::toAttendanceResponseDTO);
        
        log.debug("[API] Encontradas {} presenças", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<AttendanceResponseDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando presença com ID: {}", id);
        Attendance attendance = finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found");
        
        log.debug("[API] Presença encontrada: {}", attendance.getId());
        return CompletableFuture.completedFuture(responseMapper.toAttendanceResponseDTO(attendance));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, AttendanceUpdateDTO data) {
        log.info("[API] Atualizando presença com ID: {}", id);
        Attendance attendance = finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found");
        
        data.isAbsent().ifPresent(absent -> {
            log.debug("[API] Atualizando campo 'absent' para: {}", absent);
            attendance.setAbsent(absent);
            updateAbsences(attendance.getEnrollee());
        });

        attendanceR.save(attendance);
        log.info("[API] Presença atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Attendance");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão da presença com ID: {}", id);
        Attendance attendance = finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found");
        
        attendanceR.deleteById(id);
        updateAbsences(attendance.getEnrollee());
        
        log.info("[API] Presença deletada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Attendance");
    }

    private void ensureAttendanceNotExists(Enrollee enrollee, Lesson lesson) {
        if (attendanceR.existsByEnrolleeIdAndLessonId(enrollee.getId(), lesson.getId())) {
            log.warn("[API] Tentativa de criar presença duplicada para enrolleeId: {} e lessonId: {}", enrollee.getId(), lesson.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already exists for this Enrollee and Exam");
        }
    }

    private void updateAbsences(Enrollee enrollee) {
        int countAbsent = (int) enrollee.getAttendances().stream().filter(Attendance::isAbsent).count();
        log.debug("[API] Atualizando número de faltas para enrolleeId: {}. Total faltas: {}", enrollee.getId(), countAbsent);
        enrollee.setAbsences(countAbsent);
        enrolleeR.save(enrollee);
    }

}
