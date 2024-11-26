package com.kacademico.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.models.Course;
import com.kacademico.models.Professor;
import com.kacademico.models.Subject;
import com.kacademico.models.User;
import com.kacademico.repositories.CourseRepository;
import com.kacademico.repositories.ProfessorRepository;
import com.kacademico.repositories.SubjectRepository;
import com.kacademico.repositories.UserRepository;

@Service
public class MappingService {
    
    private final UserRepository userR;
    private final CourseRepository courseR;
    private final SubjectRepository subjectR;
    private final ProfessorRepository professorR;

    public MappingService(
        UserRepository userR,
        CourseRepository courseR,
        SubjectRepository subjectR,
        ProfessorRepository professorR) {
            this.userR = userR;
            this.courseR = courseR;
            this.subjectR = subjectR;
            this.professorR = professorR;
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

}
