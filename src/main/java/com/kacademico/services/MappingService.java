package com.kacademico.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.models.Course;
import com.kacademico.models.User;
import com.kacademico.repositories.CourseRepository;
import com.kacademico.repositories.UserRepository;

@Service
public class MappingService {
    
    private final UserRepository userR;
    private final CourseRepository courseR;

    public MappingService(
        UserRepository userR,
        CourseRepository courseR) {
            this.userR = userR;
            this.courseR = courseR;
    }

    public User findUserById(UUID id) {
        return userR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User"));
    }

    public Course findCourseById(UUID id) {
        return courseR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course"));
    }

}
