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
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findByIdWithEvaluationsAndAttendances(data.enrollee()), "Enrollee not Found");
        Lesson lesson = finder.findByIdOrThrow(lessonR.findById(data.lesson()), "Lesson not Found");

        ensureAttendanceNotExists(enrollee, lesson);
        ensureSameGrade(enrollee, lesson);

        Attendance attendance = requestMapper.toAttendance(data);
        
        attendanceR.save(attendance);
        updateAbsences(attendance.getEnrollee());
        return CompletableFuture.completedFuture("Attendance successfully Created: " + attendance.getId());
    }

    @Async
    public CompletableFuture<List<AttendanceResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(attendanceR.findAll(), responseMapper::toAttendanceResponseDTO));
    }

    @Async
    public CompletableFuture<AttendanceResponseDTO> readByIdAsync(UUID id) {        
        return CompletableFuture.completedFuture(responseMapper.toAttendanceResponseDTO(finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, AttendanceUpdateDTO data) {
        Attendance attendance = finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found");
        
        data.isAbsent().ifPresent(attendance::setAbsent);
        if (data.isAbsent().isPresent()) updateAbsences(attendance.getEnrollee());

        attendanceR.save(attendance);
        return CompletableFuture.completedFuture("Updated Attendance");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        Attendance attendance = finder.findByIdOrThrow(attendanceR.findById(id), "Attendance not Found");
        
        attendanceR.deleteById(id);
        updateAbsences(attendance.getEnrollee());
        return CompletableFuture.completedFuture("Deleted Attendance");
    }

    private void ensureAttendanceNotExists(Enrollee enrollee, Lesson lesson) {
        if (attendanceR.existsByEnrolleeIdAndLessonId(enrollee.getId(), lesson.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already exists for this Enrollee and Exam");
    }

    private void ensureSameGrade(Enrollee enrollee, Lesson lesson) {
        if (!(enrollee.getGrade().getId().equals(lesson.getGrade().getId()))) 
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enrollee and Lesson must belong to the same Grade");
    }

    private void updateAbsences(Enrollee enrollee) {
        enrollee.setAbsences( (int)enrollee.getAttendances().stream().filter(Attendance::isAbsent).count());
        enrolleeR.save(enrollee);
    }

}