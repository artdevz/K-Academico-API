package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.grade.GradeRequestDTO;
import com.kacademic.app.dto.grade.GradeResponseDTO;
import com.kacademic.app.dto.grade.GradeUpdateDTO;
import com.kacademic.domain.enums.EGrade;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.ProfessorRepository;
import com.kacademic.domain.repositories.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    private final SubjectRepository subjectR;
    private final ProfessorRepository professorR;  
    
    private final SemesterService semesterS;

    private final String entity = "Grade";

    public GradeService(GradeRepository gradeR, EnrolleeRepository enrolleeR, SubjectRepository subjectR, ProfessorRepository professorR, SemesterService semesterS) {
        this.gradeR = gradeR;
        this.enrolleeR = enrolleeR;
        this.subjectR = subjectR;
        this.professorR = professorR;
        this.semesterS = semesterS;
    }
    
    @Async
    public CompletableFuture<String> createAsync(GradeRequestDTO data) {

        Grade grade = new Grade(
            subjectR.findById(data.subject()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found")),
            professorR.findById(data.professor()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not Found")),
            data.capacity(),
            data.semester(),
            data.locate(),
            data.timetable()
        );

        gradeR.save(grade);
        return CompletableFuture.completedFuture("Created " + entity);

    }

    @Async
    public CompletableFuture<List<GradeResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            gradeR.findAll().stream()
            .map(grade -> new GradeResponseDTO(
                grade.getId(),
                grade.getSubject().getId(),
                grade.getProfessor().getId(),
                grade.getCapacity(),
                grade.getCurrentStudents(),
                grade.getSemester(),
                grade.getStatus(),
                grade.getLocate(),
                grade.getTimetables()
            ))
            .collect(Collectors.toList()));

    }

    @Async
    public CompletableFuture<GradeResponseDTO> readByIdAsync(UUID id) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        return CompletableFuture.completedFuture(
            new GradeResponseDTO(
            grade.getId(),
            grade.getSubject().getId(),
            grade.getProfessor().getId(),
            grade.getCapacity(),
            grade.getCurrentStudents(),
            grade.getSemester(),
            grade.getStatus(),
            grade.getLocate(),
            grade.getTimetables()
        ));

    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, GradeUpdateDTO data) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
    
        data.status().ifPresent(grade::setStatus);
        if (data.status().isPresent() && data.status().get().equals(EGrade.FINISHED)) semesterS.partialSubmitAsync(id);

        grade.setCurrentStudents(grade.getEnrollees().size()); // Atualiza o Número de Estudantes.
        
        gradeR.save(grade);
        return CompletableFuture.completedFuture("Updated " + entity);
        
    }

    @Transactional
    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!gradeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
