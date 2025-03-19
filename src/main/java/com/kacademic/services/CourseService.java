package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.course.CourseDetailsDTO;
import com.kacademic.dto.course.CourseRequestDTO;
import com.kacademic.dto.course.CourseResponseDTO;
import com.kacademic.dto.course.CourseUpdateDTO;
import com.kacademic.dto.subject.SubjectResponseDTO;
import com.kacademic.models.Course;
import com.kacademic.repositories.CourseRepository;

@Service
public class CourseService {
    
    private final CourseRepository courseR;

    private final String entity = "Course";

    public CourseService(CourseRepository courseR) {
        this.courseR = courseR;
    }

    public String create(CourseRequestDTO data) {
         
        Course course = new Course(
            data.name(),
            data.code(),
            data.duration(),
            data.description()
        );
        
        courseR.save(course);
        return "Created" + entity;

    }

    public List<CourseResponseDTO> readAll() {

        return courseR.findAll().stream()
            .map(course -> new CourseResponseDTO(
                course.getId(),                
                course.getName(),
                course.getCode(),
                course.getDuration(),
                course.getDescription()
            ))
            .collect(Collectors.toList());
    }

    public CourseDetailsDTO readById(UUID id) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found."));
        
        return new CourseDetailsDTO(
            new CourseResponseDTO(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getDuration(),
                course.getDescription()
                ),
            course.getSubjects().stream().map(subject -> new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getName(),
                subject.getName(),
                subject.getDescription(),
                subject.getDuration(),
                subject.getSemester(),
                subject.getPrerequisites()
                )).collect(Collectors.toList())
        );
    }

    public String update(UUID id, CourseUpdateDTO data) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));        
        
        data.name().ifPresent(course::setName);
        data.description().ifPresent(course::setDescription);

        courseR.save(course);
        return "Updated" + entity;
        
    }

    public String delete(UUID id) {

        if (!courseR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found.");
        
        courseR.deleteById(id);
        return "Deleted" + entity;

    }

}
