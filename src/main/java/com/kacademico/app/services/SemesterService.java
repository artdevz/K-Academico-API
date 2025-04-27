package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.domain.enums.EEnrollee;
import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.models.Student;
import com.kacademico.domain.repositories.EnrolleeRepository;
import com.kacademico.domain.repositories.GradeRepository;
import com.kacademico.domain.repositories.StudentRepository;
import com.kacademico.shared.utils.Semester;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SemesterService {
    
    private final float APPROVED_THRESHOLD = 7f;
    private final float PARTIAL_THRESHOLD = 5f;
    private final float FINAL_THRESHOLD = 3f;

    private final GradeRepository gradeR;
    private final StudentRepository studentR;
    private final EnrolleeRepository enrolleeR;

    private final AsyncTaskExecutor taskExecutor;

    /** 
     * Updates the students' statuses based on their partial results
     * Average >= 7 -> APROVED
     * 7 > Avarege >= 3 -> FINAL EXAM
     * 3 > Avarege -> FAILED
     */ 
    @Async("taskExecutor")
    public CompletableFuture<String> processPartialResults(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Grade grade = gradeR.findWithEnrolleesById(id).get();
    
            grade.getEnrollees().forEach(this::updatePartialStatus);
    
            return "Partial results processed successfully";
        }, taskExecutor);
    }

    /**
     * Finalizes the semester by updating grades and students' final statuses
     * Average >= 5 -> APROVED (POST FINAL EXAM)
     * 5 > Average -> FAILED
     */
    @Async("taskExecutor")
    public CompletableFuture<String> finalizeSemester(@Semester String semester) {
        return CompletableFuture.supplyAsync(() -> {
            List<Grade> grades = gradeR.findAllWithEnrolleesBySemesterAndStatus(semester, EGrade.FINAL);
    
            grades.forEach(this::finalizeGrade);
            updateStudentsAvarage();

            return "Semester finalized successfully";
        }, taskExecutor);
    }

    private void updatePartialStatus(Enrollee enrollee) {
        if (enrollee.getStatus().equals(EEnrollee.ENROLLED)) {
            enrollee.setStatus(determinePartialResult(enrollee.getAverage()));
            enrolleeR.save(enrollee);
        }
    }

    private void finalizeGrade(Grade grade) {
        grade.setStatus(EGrade.FINISHED);
        grade.getEnrollees().forEach(this::finalizeEnrollee);
        gradeR.save(grade);
    }

    private void finalizeEnrollee(Enrollee enrollee) {
        if (enrollee.getStatus() == EEnrollee.FINAL_EXAM) {
            enrollee.setStatus(determineFinalResult(enrollee.getAverage()));
            enrolleeR.save(enrollee);
        }
    }

    private EEnrollee determinePartialResult(float avarage) {
        if (avarage < FINAL_THRESHOLD) return EEnrollee.FAILED;
        if (avarage >= APPROVED_THRESHOLD) return EEnrollee.APPROVED;
        return EEnrollee.FINAL_EXAM;
    }

    private EEnrollee determineFinalResult(float avarage) {
        return (avarage < PARTIAL_THRESHOLD) ? EEnrollee.FAILED : EEnrollee.APPROVED;
    }

    private void updateStudentsAvarage() {
        studentR.findAllWithEnrollees().forEach(this::updateStudentAvarage);
    }

    private void updateStudentAvarage(Student student) {
        double average = student.getEnrollees().stream()
            .mapToDouble(Enrollee::getAverage)
            .average()
            .orElse(0.0);

        student.setAverage((float)average);
        studentR.save(student);
    }

}