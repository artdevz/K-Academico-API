package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.domain.enums.EEnrollee;
import com.kacademic.domain.enums.EGrade;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.models.Student;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.StudentRepository;
import com.kacademic.shared.utils.Semester;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SemesterService {
    
    private final float APPROVED = 7f;
    private final float PARTIAL = 5f;
    private final float FINAL = 3f;

    private final GradeRepository gradeR;
    private final StudentRepository studentR;
    private final EnrolleeRepository enrolleeR;

    private final AsyncTaskExecutor taskExecutor;

    // PRE AF
    @Async("taskExecutor")
    public CompletableFuture<String> partialSubmitAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Grade grade = gradeR.findById(id).get();
    
            for (Enrollee enrollee : grade.getEnrollees()) {
                if (enrollee.getStatus().equals(EEnrollee.ENROLLED)) enrollee.setStatus( getPartialResult(enrollee.getAverage() ));
                // SUSPENDED... (Disciplina Trancada)?
                enrolleeR.save(enrollee);
            }
    
            // return CompletableFuture.completedFuture("Partially completed the Grade Activities");
            return "Partially completed the Grade Activities";
        }, taskExecutor);
    }

    // POS AF
    @Async("taskExecutor")
    public CompletableFuture<String> finalSubmitAsync(@Semester String semester) {
        return CompletableFuture.supplyAsync(() -> {
            List<Grade> grades = gradeR.findAll();
    
            for (Grade grade : grades) {
                if (grade.getStatus().equals(EGrade.ONGOING) && grade.getSemester().equals(semester)) grade.setStatus(EGrade.FINISHED);
    
                for (Enrollee enrollee : grade.getEnrollees()) {
                    if (enrollee.getStatus().equals(EEnrollee.FINAL_EXAM)) enrollee.setStatus( getFinalResult(enrollee.getAverage()) );
                    
                    enrolleeR.save(enrollee);
                }
                gradeR.save(grade);
            }
    
            this.updateAvarage();
            // return CompletableFuture.completedFuture("All Grades have been completed for Semester ");
            return "All Grades have been completed for Semester ";
        }, taskExecutor);
    }

    // Result
    private EEnrollee getPartialResult(float avarage) {
        if (avarage < FINAL) return EEnrollee.FAILED;
        if (avarage >= APPROVED) return EEnrollee.APPROVED;

        return EEnrollee.FINAL_EXAM;
    }

    private EEnrollee getFinalResult(float avarage) {
        if (avarage < PARTIAL) return EEnrollee.FAILED;

        return EEnrollee.APPROVED;
    }

    // Avarage
    private void updateAvarage() {
        for (Student student : studentR.findAll() ) { 
            student.setAverage(calculateAvarage(student));

            studentR.save(student);
        }
    }

    private float calculateAvarage(Student student) {
        float sum = 0;
        for (Enrollee enrollee : student.getEnrollees() ) sum += enrollee.getAverage();
        
        return sum / student.getEnrollees().size();
    }

}