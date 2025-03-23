package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.enums.EEnrollee;
import com.kacademic.enums.EGrade;
import com.kacademic.models.Enrollee;
import com.kacademic.models.Grade;
import com.kacademic.models.Student;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.StudentRepository;
import com.kacademic.utils.Semester;

@Service
public class SemesterService {
    
    private final float APPROVED = 7f;
    private final float PARTIAL = 5f;
    private final float FINAL = 3f;

    private final GradeRepository gradeR;
    private final StudentRepository studentR;
    private final EnrolleeRepository enrolleeR;

    public SemesterService(GradeRepository gradeR, StudentRepository studentR, EnrolleeRepository enrolleeR) {
        this.gradeR = gradeR;
        this.studentR = studentR;
        this.enrolleeR = enrolleeR;
    }

    // PRE AF
    @Async
    public CompletableFuture<String> partialSubmitAsync(UUID id) {
        
        Grade grade = gradeR.findById(id).get();

        for (Enrollee enrollee : grade.getEnrollees()) {

            if (enrollee.getStatus().equals(EEnrollee.ENROLLED)) enrollee.setStatus( getPartialResult(enrollee.getAverage() ));
            // SUSPENDED... (Disciplina Trancada)?
            enrolleeR.save(enrollee);

        }

        return CompletableFuture.completedFuture("Partially completed the Grade Activities");
        
    }

    // POS AF
    @Async
    public CompletableFuture<String> finalSubmitAsync(@Semester String semester) {
     
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
        return CompletableFuture.completedFuture("All Grades have been completed for Semester ");

    }

    // Result
    public EEnrollee getPartialResult(float avarage) {

        if (avarage < FINAL) return EEnrollee.FAILED;
        if (avarage >= APPROVED) return EEnrollee.APPROVED;
        return EEnrollee.FINAL_EXAM;
        
    }

    public EEnrollee getFinalResult(float avarage) {

        if (avarage < PARTIAL) return EEnrollee.FAILED;
        return EEnrollee.APPROVED;

    }

    // Avarage
    public void updateAvarage() {

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
