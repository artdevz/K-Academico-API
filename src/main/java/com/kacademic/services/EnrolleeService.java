package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.models.Enrollee;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.StudentRepository;

@Service
public class EnrolleeService {
    
    private final EnrolleeRepository enrolleeR;
    private final StudentRepository studentR;
    private final GradeRepository gradeR;

    private final String entity = "Enrollee";

    public EnrolleeService(EnrolleeRepository enrolleeR, StudentRepository studentR, GradeRepository gradeR) {
        this.enrolleeR = enrolleeR;
        this.studentR = studentR;
        this.gradeR = gradeR;
    }
    
    @Async
    public CompletableFuture<String> createAsync(EnrolleeRequestDTO data) {

        Enrollee enrollee = new Enrollee(
            studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found")),
            studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student.Transcript not Found")).getTranscript(),
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"))
        );

        enrollee.getGrade().setCurrentStudents( // Adiciona 1 Estudante na Turma
            enrollee.getGrade().getCurrentStudents() + 1 
        );
        enrolleeR.save(enrollee);
        return CompletableFuture.completedFuture("Created " + entity);

    }

    @Async
    public CompletableFuture<List<EnrolleeResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            enrolleeR.findAll().stream()
            .map(enrollee -> new EnrolleeResponseDTO(
                enrollee.getId(),
                enrollee.getStudent().getId(),
                // enrollee.getGrade() != null ? enrollee.getGrade().getSubject().getName() : "Unavailable",
                enrollee.getGrade().getId(),
                enrollee.getAbsences(),
                enrollee.getAverage(),
                enrollee.getStatus()
            ))
            .collect(Collectors.toList()));

    }

    @Async
    public CompletableFuture<EnrolleeDetailsDTO> readByIdAsync(UUID id) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        return CompletableFuture.completedFuture(
            new EnrolleeDetailsDTO(
                new EnrolleeResponseDTO(
                    enrollee.getId(),
                    enrollee.getStudent().getId(),
                    enrollee.getGrade().getId(),
                    enrollee.getAbsences(),
                    enrollee.getAverage(),
                    enrollee.getStatus()
                    ),
                
                enrollee.getEvaluations().stream().map(evaluation -> new EvaluationResponseDTO(
                    evaluation.getId(),
                    evaluation.getEnrollee().getId(),
                    evaluation.getExam().getGrade().getId(),
                    evaluation.getExam().getId(),
                    evaluation.getScore()
                    ))
                    .collect(Collectors.toList()),

                enrollee.getAttendances().stream().map(attendance -> new AttendanceResponseDTO(
                    attendance.getId(),
                    attendance.getEnrollee().getId(),
                    attendance.getLesson().getId(),
                    attendance.isAbsent()
                    )).collect(Collectors.toList())
            
        ));

    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EnrolleeUpdateDTO data) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
            
        enrolleeR.save(enrollee);
        return CompletableFuture.completedFuture("Updated " + entity);
        
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!enrolleeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found");

        enrolleeR.findById(id).get().getGrade().setCurrentStudents( // Remove 1 Estudante na Turma
            enrolleeR.findById(id).get().getGrade().getCurrentStudents() - 1
        );
        enrolleeR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
