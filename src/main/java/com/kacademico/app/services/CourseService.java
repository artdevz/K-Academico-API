package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.course.CourseDetailsDTO;
import com.kacademico.app.dto.course.CourseRequestDTO;
import com.kacademico.app.dto.course.CourseResponseDTO;
import com.kacademico.app.dto.course.CourseUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Course;
import com.kacademico.domain.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {
    
    private final CourseRepository courseR;
    private final EntityFinder finder;
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
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(courseR.findAll(), responseMapper::toCourseResponseDTO));
    }

    @Async
    public CompletableFuture<CourseDetailsDTO> readByIdAsync(UUID id) {
        Course course = finder.findByIdOrThrow(courseR.findWithSubjectsById(id), "Course not Found");
            
        return CompletableFuture.completedFuture(
            new CourseDetailsDTO(
                responseMapper.toCourseResponseDTO(course),
                responseMapper.toResponseDTOList(course.getSubjects(), responseMapper::toSubjectResponseDTO)
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, CourseUpdateDTO data) {
        Course course = finder.findByIdOrThrow(courseR.findById(id), "Course not Found"); 
    
        data.name().ifPresent(course::setName);
        data.description().ifPresent(course::setDescription);

        courseR.save(course);
        return CompletableFuture.completedFuture("Updated Course");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(courseR.findById(id), "Course not Found");        
        courseR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Course");
    }

}