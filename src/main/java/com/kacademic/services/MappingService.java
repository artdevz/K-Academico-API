package com.kacademic.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.models.Course;
import com.kacademic.models.Enrollee;
import com.kacademic.models.Exam;
import com.kacademic.models.Grade;
import com.kacademic.models.Professor;
import com.kacademic.models.Student;
import com.kacademic.models.Subject;
import com.kacademic.models.User;
import com.kacademic.repositories.CourseRepository;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.ExamRepository;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.ProfessorRepository;
import com.kacademic.repositories.StudentRepository;
import com.kacademic.repositories.SubjectRepository;
import com.kacademic.repositories.UserRepository;

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
