package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.grade.GradeRequestDTO;
import com.kacademico.app.dto.grade.GradeResponseDTO;
import com.kacademico.app.dto.grade.GradeUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.models.values.Schedule;
import com.kacademico.domain.models.values.Timetable;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.domain.repositories.IGradeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GradeService {
    
    private final IGradeRepository gradeR;
    private final IEnrolleeRepository enrolleeR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;  
    
    private final SemesterService semesterS;
    
    @Async
    public CompletableFuture<String> createAsync(GradeRequestDTO data) {
        log.info("[API] Iniciando criação de turma para subjectId: {}", data.subject());

        ensureTimeTableAndLocationAreNotConflicting(data.timetable(), data.schedule());
        ensureProfessorIsAvailableAtTime(data.timetable(), data.schedule(), data.professor());

        Grade grade = requestMapper.toGrade(data);
        Grade saved = gradeR.save(grade);

        log.info("[API] Turma criada com sucesso. ID: {}", saved);
        return CompletableFuture.completedFuture("Grade successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<GradeResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as turmas");
        List<GradeResponseDTO> response = responseMapper.toResponseDTOList(gradeR.findAll(), responseMapper::toGradeResponseDTO);

        log.debug("[API] Encontradas {} turmas", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<GradeResponseDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscanto todas as turmas");
        Grade grade = finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
        
        log.debug("[API] Encontradas {} turmas", grade.getId());
        return CompletableFuture.completedFuture(responseMapper.toGradeResponseDTO(grade));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, GradeUpdateDTO data) {
        log.info("[API] Atualizando turma com ID: {}", id);
        Grade grade = finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
    
        data.status().ifPresent(status -> {
            log.debug("[API] Atualizando campo 'status' para {}", status.name());
            grade.setStatus(status);
            if (status.equals(EGrade.FINAL)) semesterS.processPartialResults(id);
        });
        
        gradeR.save(grade);
        log.info("[API] Turma atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Grade");
    }

    @Transactional
    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão da turma com ID: {}", id);
        finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);

        log.info("[API] Turma deletada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Grade");
    }

    private void ensureTimeTableAndLocationAreNotConflicting(List<Timetable> newTimetables, Schedule schedule) {
        log.debug("[API] Verificando conflitos de horário e local: semestre: {}, local: {}", schedule.getSemester(), schedule.getLocate());

        List<Grade> gradesAtSameLocation = findGradesBySemesterAndLocation(schedule.getSemester(), schedule.getLocate());
    
        checkForTimetableConflicts(gradesAtSameLocation, newTimetables, "Timetable already used at this location");
    }
    
    private void ensureProfessorIsAvailableAtTime(List<Timetable> newTimetables, Schedule schedule, UUID professorId) {
        log.debug("[API] Verificando disponibilidade do professor {} no semestre {}", professorId, schedule.getSemester());

        List<Grade> gradesWithSameProfessor = findGradesBySemesterAndProfessor(schedule.getSemester(), professorId);
    
        checkForTimetableConflicts(gradesWithSameProfessor, newTimetables, "Professor is unavailable at this time");
    }

    private void checkForTimetableConflicts(List<Grade> grades, List<Timetable> newTimetables, String errorMessage) {
        for (Grade grade : grades) for (Timetable existing : grade.getTimetables()) for (Timetable incoming : newTimetables)
            if (existing.conflictWith(incoming)) {
                log.warn("[API] Conflito detectado entre horários: existente: {} vs novo: {}", existing, incoming);
                throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
            }
    }

    private List<Grade> findGradesBySemesterAndLocation(String semester, String location) {
        log.debug("[API] Buscando turmas para semestre: {} e local: {}", semester, location);

        return gradeR.findAll().stream()
            .filter(grade -> semester.equals(grade.getSchedule().getSemester())
                && location.equals(grade.getSchedule().getLocate())
            )
            .toList();
    }
    
    private List<Grade> findGradesBySemesterAndProfessor(String semester, UUID professorId) {
        log.debug("[API] Buscando turmas para semestre: {} com professor: {}", semester, professorId);

        return gradeR.findAll().stream()
            .filter(grade -> semester.equals(grade.getSchedule().getSemester())
                && professorId.equals(grade.getProfessor().getId())
            )
            .toList();
    }

}