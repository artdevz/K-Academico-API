package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.course.CourseDetailsDTO;
import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.course.CourseResponseDTO;
import com.kacademic.app.dto.course.CourseUpdateDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.repositories.CourseRepository;

@Service
public class CourseService {
    
    private final CourseRepository courseR;

    public CourseService(CourseRepository courseR) {
        this.courseR = courseR;
    }

    // @Async
    public String createAsync(CourseRequestDTO data) {
        System.out.println("[app.services.CourseService]: Criando Course...");
        Course course = new Course(
            data.name(),
            data.code(),
            data.description()
        );

        courseR.save(course);
        System.out.println("[app.services.CourseService]: Course criado!");
        // return CompletableFuture.completedFuture("Created Course");
        return "Created Course";
    }

    // @Async
    public List<CourseResponseDTO> readAllAsync() {

        return (
            courseR.findAll().stream()
            .map(course -> new CourseResponseDTO(
                course.getId(),                
                course.getName(),
                course.getCode(),
                course.getDuration(),
                course.getDescription()
            ))
            .collect(Collectors.toList()));
            
    }

    // @Async
    public CourseDetailsDTO readByIdAsync(UUID id) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
        
        return (
            new CourseDetailsDTO(
                new CourseResponseDTO(
                    course.getId(),
                    course.getName(),
                    course.getCode(),
                    course.getDuration(),
                    course.getDescription()
                ),
                course.getSubjects().stream().map(subject -> new SubjectResponseDTO(
                    subject.getId(),
                    subject.getCourse().getId(),
                    subject.getName(),
                    subject.getDescription(),
                    subject.getDuration(),
                    subject.getSemester(),
                    subject.getPrerequisites()
                )).collect(Collectors.toList())
        ));
    }

    // @Async
    public String updateAsync(UUID id, CourseUpdateDTO data) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));        
        
        data.name().ifPresent(course::setName);
        data.description().ifPresent(course::setDescription);

        courseR.save(course);
        // return CompletableFuture.completedFuture("Updated Course");
        return "Updated Course";
    }

    // @Async
    public String deleteAsync(UUID id) {

        if (!courseR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found");
        
        courseR.deleteById(id);
        // return CompletableFuture.completedFuture("Deleted Course");
        return "Deleted Course";
    }
}