package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
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
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(EnrolleeRequestDTO data) {
        Enrollee enrollee = requestMapper.toEnrollee(data);

        validateGradeStatus(enrollee.getGrade());
        ensureStudentIsUnique(enrollee.getStudent());
        
        enrolleeR.save(enrollee);
        updateGrade(enrollee.getGrade());
        return CompletableFuture.completedFuture("Enrollee successfully Created: " + enrollee.getId());
    }

    @Async
    public CompletableFuture<List<EnrolleeResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(enrolleeR.findAll(), responseMapper::toEnrolleeResponseDTO));
    }

    @Transactional
    @Async
    public CompletableFuture<EnrolleeDetailsDTO> readByIdAsync(UUID id) {
            Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findById(id), "Enrollee not Found");
            
            return CompletableFuture.completedFuture(
                new EnrolleeDetailsDTO(
                    responseMapper.toEnrolleeResponseDTO(enrollee),
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
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EnrolleeUpdateDTO data) {
            Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findById(id), "Enrollee not Found");
                
            enrolleeR.save(enrollee);
            return CompletableFuture.completedFuture("Updated Enrollee");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
            Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findByIdWithEvaluationsAndAttendances(id), "Enrollee not Found");
            
            enrolleeR.deleteById(id);
            updateGrade(enrollee.getGrade());
            return CompletableFuture.completedFuture("Deleted Enrollee");
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