package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.attendance.AttendanceResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.domain.enums.EGrade;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EnrolleeService {
    
    private final EnrolleeRepository enrolleeR;
    private final StudentRepository studentR;
    private final GradeRepository gradeR;

    private final AsyncTaskExecutor taskExecutor;
    
    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(EnrolleeRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = new Enrollee(
                studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found")),
                gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"))
            );
    
            validateGradeStatus(enrollee.getGrade());
            updateGradeCurrentStudents(enrollee.getGrade(), true);
            
            enrolleeR.save(enrollee);
            return "Created Enrollee";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<EnrolleeResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return (
                enrolleeR.findAll().stream()
                .map(enrollee -> new EnrolleeResponseDTO(
                    enrollee.getId(),
                    enrollee.getStudent().getId(),
                    enrollee.getGrade().getId(),
                    enrollee.getAbsences(),
                    enrollee.getAverage(),
                    enrollee.getStatus()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Transactional
    @Async("taskExecutor")
    public CompletableFuture<EnrolleeDetailsDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = enrolleeR.findByIdWithEvaluationsAndAttendances(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
            
            return (
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
                    )).collect(Collectors.toList()),
                    enrollee.getAttendances().stream().map(attendance -> new AttendanceResponseDTO(
                        attendance.getId(),
                        attendance.getEnrollee().getId(),
                        attendance.getLesson().getId(),
                        attendance.isAbsent()
                    )).collect(Collectors.toList())
                )
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, EnrolleeUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = enrolleeR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
                
            enrolleeR.save(enrollee);
            return "Updated Enrollee";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = enrolleeR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
    
            updateGradeCurrentStudents(enrollee.getGrade(), false);
            
            enrolleeR.deleteById(id);
            return "Deleted Enrollee";
        }, taskExecutor);
    }

    private void validateGradeStatus(Grade grade) {
        if(grade.getStatus() == EGrade.FINISHED) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot enroll a student in a finished class");
    }

    private void updateGradeCurrentStudents(Grade grade, boolean isAdding) {
        grade.setCurrentStudents(grade.getCurrentStudents() + (isAdding? 1 : -1));
        gradeR.save(grade);
    }

}