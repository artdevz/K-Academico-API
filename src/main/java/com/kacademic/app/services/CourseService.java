package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
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

    // private final Executor asyncExecutor;

    public CourseService(CourseRepository courseR) { //, @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.courseR = courseR;
        // this.asyncExecutor = asyncExecutor;
    }

    @Async
    public CompletableFuture<String> createAsync(CourseRequestDTO data) {
        System.out.println("[app.services.CourseService]: Criando Course...");
        Course course = new Course(
            data.name(),
            data.code(),
            data.description()
        );

        courseR.save(course);
        System.out.println("[app.services.CrouseService]: Course!");
        return CompletableFuture.completedFuture("Created Course");

    }

    @Async
    public CompletableFuture<List<CourseResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
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

    @Async
    public CompletableFuture<CourseDetailsDTO> readByIdAsync(UUID id) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
        
        return CompletableFuture.completedFuture(
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

    @Async
    public CompletableFuture<String> updateAsync(UUID id, CourseUpdateDTO data) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));        
        
        data.name().ifPresent(course::setName);
        data.description().ifPresent(course::setDescription);

        courseR.save(course);
        return CompletableFuture.completedFuture("Updated Course");
        
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!courseR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found");
        
        courseR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Course");

    }

}
