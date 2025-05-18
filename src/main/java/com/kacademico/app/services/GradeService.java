package com.kacademico.app.services;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
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
        ensureStartTimeIsBeforeEndTime(data.timetable());
        ensureNoInternalTimetableConflicts(data.timetable());
        ensureTimeTableAndLocationAreNotConflicting(data.timetable(), data.schedule());
        ensureProfessorIsAvailableAtTime(data.timetable(), data.schedule(), data.professor());

        Grade grade = requestMapper.toGrade(data);

        gradeR.save(grade);
        return CompletableFuture.completedFuture("Grade successfully Created: " + grade.getId());
    }

    @Async
    public CompletableFuture<List<GradeResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(gradeR.findAll(), responseMapper::toGradeResponseDTO));
    }

    @Async
    public CompletableFuture<GradeResponseDTO> readByIdAsync(UUID id) {        
        return CompletableFuture.completedFuture(responseMapper.toGradeResponseDTO(finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, GradeUpdateDTO data) {
        Grade grade = finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
    
        data.status().ifPresent(grade::setStatus);
        if (data.status().isPresent() && data.status().get().equals(EGrade.FINAL)) semesterS.processPartialResults(id);
        
        gradeR.save(grade);
        return CompletableFuture.completedFuture("Updated Grade");
    }

    @Transactional
    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Grade");
    }

    private void ensureStartTimeIsBeforeEndTime(List<Timetable> timetables) {
        timetables.stream()
            .filter(t -> !t.getStartTime().isBefore(t.getEndTime()))
            .findFirst()
            .ifPresent(t -> {
                String message = String.format(
                    "Invalid Timetable: at [%s] StartTime [%s] must be before EndTime [%s]",
                    t.getDay().getDisplayName(TextStyle.FULL, Locale.ENGLISH), t.getStartTime(), t.getEndTime() 
                );
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message);
            });
    }

    private void ensureNoInternalTimetableConflicts(List<Timetable> timetables) {
        for (int i = 0; i < timetables.size(); i++) {
            for (int j = i + 1; j < timetables.size(); j++) {
                if (timetables.get(i).conflictWith(timetables.get(j))) throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflicting Timetables in request");
            }
        }
    }

    private void ensureTimeTableAndLocationAreNotConflicting(List<Timetable> newTimetables, Schedule schedule) {
        List<Grade> gradesAtSameLocation = findGradesBySemesterAndLocation(schedule.getSemester(), schedule.getLocate());
    
        checkForTimetableConflicts(gradesAtSameLocation, newTimetables, "Timetable already used at this location");
    }
    
    private void ensureProfessorIsAvailableAtTime(List<Timetable> newTimetables, Schedule schedule, UUID professorId) {
        List<Grade> gradesWithSameProfessor = findGradesBySemesterAndProfessor(schedule.getSemester(), professorId);
    
        checkForTimetableConflicts(gradesWithSameProfessor, newTimetables, "Professor is unavailable at this time");
    }

    private void checkForTimetableConflicts(List<Grade> grades, List<Timetable> newTimetables, String errorMessage) {
        for (Grade grade : grades) for (Timetable existing : grade.getTimetables()) for (Timetable incoming : newTimetables)
            if (existing.conflictWith(incoming)) throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
    }

    private List<Grade> findGradesBySemesterAndLocation(String semester, String location) {
        return gradeR.findAll().stream()
            .filter(grade -> semester.equals(grade.getSchedule().getSemester())
                && location.equals(grade.getSchedule().getLocate())
            )
            .toList();
    }
    
    private List<Grade> findGradesBySemesterAndProfessor(String semester, UUID professorId) {
        return gradeR.findAll().stream()
            .filter(grade -> semester.equals(grade.getSchedule().getSemester())
                && professorId.equals(grade.getProfessor().getId())
            )
            .toList();
    }

}