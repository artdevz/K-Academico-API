package com.kacademico.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kacademico.enums.EEnrollee;
import com.kacademico.enums.EGrade;
import com.kacademico.models.Enrollee;
import com.kacademico.models.Grade;
import com.kacademico.models.Student;
import com.kacademico.repositories.EnrolleeRepository;
import com.kacademico.repositories.GradeRepository;
import com.kacademico.repositories.StudentRepository;
import com.kacademico.utils.Semester;

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
    public void partialSubmit(UUID id) {
        
        Grade grade = gradeR.findById(id).get();

        for (Enrollee enrollee : grade.getEnrollees()) {

            if (enrollee.getStatus().equals(EEnrollee.ENROLLED)) enrollee.setStatus( getPartialResult(enrollee.getAvarage() ));
            // SUSPENDED... (Disciplina Trancada)?
            enrolleeR.save(enrollee);

        }
        
    }

    // POS AF
    public void finalSubmit(@Semester String semester) {
     
        List<Grade> grades = gradeR.findAll();

        for (Grade grade : grades) {
        
            if (grade.getStatus().equals(EGrade.ONGOING) && grade.getSemester().equals(semester)) grade.setStatus(EGrade.FINISHED);
            
            for (Enrollee enrollee : grade.getEnrollees()) {
                
                if (enrollee.getStatus().equals(EEnrollee.FINAL_EXAM)) enrollee.setStatus( getFinalResult(enrollee.getAvarage()) );
                
                enrolleeR.save(enrollee);

            }

            gradeR.save(grade);

        }

        this.updateAvarage();

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
            student.setAvarage(calculateAvarage(student));
            studentR.save(student);
        }
    }

    private float calculateAvarage(Student student) {
        
        float sum = 0;
        for (Enrollee enrollee : student.getEnrollees() ) sum += enrollee.getAvarage();
        
        return sum / student.getEnrollees().size();

    }

}
