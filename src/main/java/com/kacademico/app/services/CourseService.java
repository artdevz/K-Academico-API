package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kacademico.app.dto.course.CourseDetailsDTO;
import com.kacademico.app.dto.course.CourseRequestDTO;
import com.kacademico.app.dto.course.CourseResponseDTO;
import com.kacademico.app.dto.course.CourseUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Course;
import com.kacademico.domain.repositories.ICourseRepository;
import com.kacademico.shared.utils.EnsureUniqueUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {
    
    private final ICourseRepository courseR;
    private final EntityFinder finder;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @Async
    public CompletableFuture<String> createAsync(CourseRequestDTO data) {
        EnsureUniqueUtil.ensureUnique(() -> courseR.findByCode(data.code()), () -> "A course with code " + data.code() + " already exists");        
        EnsureUniqueUtil.ensureUnique(() -> courseR.findByName(data.name()), () -> "A course with name " + data.name() + " already exists");

        Course course = requestMapper.toCourse(data);
        Course saved = courseR.save(course);

        return CompletableFuture.completedFuture("Course successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<CourseResponseDTO>> readAllAsync() {
        List<Course> courses = courseR.findAll();
        List<CourseResponseDTO> response = responseMapper.toResponseDTOList(courses, responseMapper::toCourseResponseDTO);
        return CompletableFuture.completedFuture(response);
    }

    @Transactional
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

        if (data.name().isPresent()) {
            EnsureUniqueUtil.ensureUnique(() -> courseR.findByName(data.name().get()), () -> "A course with name " + data.name().get() + " already exists");
            course.setName(data.name().get());
        }
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