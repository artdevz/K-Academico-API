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
import com.kacademic.domain.models.Student;
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
                findStudent(data.student()),
                findGradeDetails(data.grade())
            );
    
            validateGradeStatus(enrollee.getGrade());
            ensureStudentIsUnique(enrollee.getStudent());
            
            enrolleeR.save(enrollee);
            updateGrade(enrollee.getGrade());
            return "Enrollee successfully Created: " + enrollee.getId();
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
            Enrollee enrollee = findEnrolleeDetails(id);
            
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
            Enrollee enrollee = findEnrollee(id);
                
            enrolleeR.save(enrollee);
            return "Updated Enrollee";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = findEnrolleeDetails(id);
            
            enrolleeR.deleteById(id);
            updateGrade(enrollee.getGrade());
            return "Deleted Enrollee";
        }, taskExecutor);
    }

    private Enrollee findEnrollee(UUID id) {
        return enrolleeR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
    }

    private Enrollee findEnrolleeDetails(UUID id) {
        return enrolleeR.findByIdWithEvaluationsAndAttendances(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
    }

    private Student findStudent(UUID id) {
        return studentR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found"));
    }

    private Grade findGradeDetails(UUID id) {
        return gradeR.findByIdWithEnrollees(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"));
    }

    private void validateGradeStatus(Grade grade) {
        if(grade.getStatus() == EGrade.FINISHED) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot enroll a student in a finished class");
    }

    private void ensureStudentIsUnique(Student student) {
        if(studentR.findById(student.getId()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists for this Grade");
    }

    private void updateGrade(Grade grade) {
        grade.setCurrentStudents( (int)grade.getEnrollees().stream().count());
        System.out.println("Grade Current Students: " + grade.getCurrentStudents());
        System.out.println("GradeEnrollees: " + (int)grade.getEnrollees().stream().count());
        gradeR.save(grade);
    }

}