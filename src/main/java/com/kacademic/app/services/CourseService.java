package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.course.CourseDetailsDTO;
import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.course.CourseResponseDTO;
import com.kacademic.app.dto.course.CourseUpdateDTO;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {
    
    private final CourseRepository courseR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;


    @Async
    public CompletableFuture<String> createAsync(CourseRequestDTO data) {
        Course course = requestMapper.toCourse(data);

        courseR.save(course);
        return CompletableFuture.completedFuture("Course successfully Created: " + course.getId());
    }

    @Async
    public CompletableFuture<List<CourseResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toCourseResponseDTOList(courseR.findAll()));
    }

    @Async
    public CompletableFuture<CourseDetailsDTO> readByIdAsync(UUID id) {
        Course course = findWithSubjectsById(id);
            
        return CompletableFuture.completedFuture(
            new CourseDetailsDTO(
                responseMapper.toCourseResponseDTO(course),
                responseMapper.toSubjectResponseDTOList(course.getSubjects())
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, CourseUpdateDTO data) {
        Course course = findCourse(id);
    
        data.name().ifPresent(course::setName);
        data.description().ifPresent(course::setDescription);

        courseR.save(course);
        return CompletableFuture.completedFuture("Updated Course");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        findCourse(id);        
        courseR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Course");
    }


    private Course findCourse(UUID id) {
        return courseR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
    }

    private Course findWithSubjectsById(UUID id) {
        return courseR.findWithSubjectsById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
    }

}