package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {
    
    private final CourseRepository courseR;
    
    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(CourseRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Course course = new Course(
                data.name(),
                data.code(),
                data.description()
            );

            courseR.save(course);
            return "Course successfully Created: " + course.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<CourseResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return (
                courseR.findAll().stream()
                .map(course -> new CourseResponseDTO(
                    course.getId(),                
                    course.getName(),
                    course.getCode(),
                    course.getDuration(),
                    course.getDescription()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Transactional
    @Async("taskExecutor")
    public CompletableFuture<CourseDetailsDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Course course = findCourseWithDetails(id);
            
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
                        subject.isRequired()
                    )).collect(Collectors.toList())
                )
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, CourseUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Course course = findCourse(id);
        
            data.name().ifPresent(course::setName);
            data.description().ifPresent(course::setDescription);

            courseR.save(course);
            return "Updated Course";
        }, taskExecutor);
        
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            findCourse(id);
            
            courseR.deleteById(id);
            return "Deleted Course";
        }, taskExecutor);
    }

    private Course findCourse(UUID id) {
        return courseR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
    }

    private Course findCourseWithDetails(UUID id) {
        return courseR.findByIdWithSubjects(id).get();
    }

}