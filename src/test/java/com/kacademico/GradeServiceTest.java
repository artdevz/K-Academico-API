package com.kacademico;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kacademico.models.Enrollee;
import com.kacademico.models.Grade;
import com.kacademico.models.Student;
import com.kacademico.models.Subject;
import com.kacademico.models.User;
import com.kacademico.repositories.EnrolleeRepository;
import com.kacademico.repositories.GradeRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class GradeServiceTest {
    
    @Autowired
    private EnrolleeRepository enrolleeR;

    @Autowired
    private GradeRepository gradeR;

    @Test
    @Transactional
    void testCascadeRemove() {

        Subject subject = new Subject();
        Student student1 = new Student();
        Student student2 = new Student();
        User user1 = new User();
        User user2 = new User();

        Grade grade = new Grade();
        grade.setSubject(subject);
        grade.getSubject().setName("Álgebra Linear");

        Enrollee enrollee1 = new Enrollee();
        enrollee1.setStudent(student1);
        enrollee1.getStudent().setUser(user1);
        enrollee1.getStudent().getUser().setName("Joãozinho");
        enrollee1.setGrade(grade);

        Enrollee enrollee2 = new Enrollee();
        enrollee2.setStudent(student2);
        enrollee2.getStudent().setUser(user2);
        enrollee2.getStudent().getUser().setName("Mariazinha");
        enrollee2.setGrade(grade);

        grade.getEnrollees().add(enrollee1);
        grade.getEnrollees().add(enrollee2);

        gradeR.save(grade);

        gradeR.delete(grade);

        assertFalse(gradeR.findById(grade.getId()).isPresent());

        assertTrue(enrolleeR.findById(enrollee1.getId()).isEmpty());
        assertTrue(enrolleeR.findById(enrollee2.getId()).isEmpty());

    }

}
