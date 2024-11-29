package com.kacademico.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.models.Course;
import com.kacademico.models.Enrollee;
import com.kacademico.models.Exam;
import com.kacademico.models.Grade;
import com.kacademico.models.Professor;
import com.kacademico.models.Student;
import com.kacademico.models.Subject;
import com.kacademico.models.User;
import com.kacademico.repositories.CourseRepository;
import com.kacademico.repositories.EnrolleeRepository;
import com.kacademico.repositories.ExamRepository;
import com.kacademico.repositories.GradeRepository;
import com.kacademico.repositories.ProfessorRepository;
import com.kacademico.repositories.StudentRepository;
import com.kacademico.repositories.SubjectRepository;
import com.kacademico.repositories.UserRepository;

@Service
public class MappingService {
    
    private final UserRepository userR;
    private final CourseRepository courseR;
    private final SubjectRepository subjectR;
    private final ProfessorRepository professorR;
    private final StudentRepository studentR;
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    private final ExamRepository examR;

    public MappingService(
        UserRepository userR,
        CourseRepository courseR,
        SubjectRepository subjectR,
        ProfessorRepository professorR,
        StudentRepository studentR,
        GradeRepository gradeR,
        EnrolleeRepository enrolleeR,
        ExamRepository examR) {
            this.userR = userR;
            this.courseR = courseR;
            this.subjectR = subjectR;
            this.professorR = professorR;
            this.studentR = studentR;
            this.gradeR = gradeR;
            this.enrolleeR = enrolleeR;
            this.examR = examR;
    }

    public User findUserById(UUID id) {
        return userR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User"));
    }

    public Course findCourseById(UUID id) {
        return courseR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course"));
    }

    public Subject findSubjectById(UUID id) {
        return subjectR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject"));
    }

    public Professor findProfessorById(UUID id) {
        return professorR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor"));
    }

    public Student findStudentById(UUID id) {
        return studentR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student"));
    }

    public Grade findGradeById(UUID id) {
        return gradeR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade"));
    }

    public Enrollee findEnrolleeById(UUID id) {
        return enrolleeR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee"));
    }

    public Exam findExamById(UUID id) {
        return examR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam"));
    }

}
