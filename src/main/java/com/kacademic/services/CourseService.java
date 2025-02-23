package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.course.CourseRequestDTO;
import com.kacademic.dto.course.CourseResponseDTO;
import com.kacademic.models.Course;
import com.kacademic.repositories.CourseRepository;

@Service
public class CourseService {
    
    private final CourseRepository courseR;

    public CourseService(CourseRepository courseR) {
        this.courseR = courseR;
    }

    public void create(CourseRequestDTO data) {
         
        Course course = new Course(
            data.name(),
            data.code(),
            data.duration(),
            data.description()
        );
        
        courseR.save(course);

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

    public CourseResponseDTO readById(UUID id) {

        Course course = courseR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado."));
        
        return new CourseResponseDTO(
            course.getId(),                
            course.getName(),
            course.getCode(),
            course.getDuration(),
            course.getDescription()
        );
    }

    public Course update(UUID id, Map<String, Object> fields) {

        Optional<Course> existingCourse = courseR.findById(id);
    
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {

                    case "name":
                        String name = (String) value;
                        course.setName(name);
                        break;                    

                    default:
                        Field field = ReflectionUtils.findField(Course.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, course, value);
                        }
                        break;
                }
            });
            
            return courseR.save(course);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado.");
        
    }

    public void delete(UUID id) {

        if (!courseR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado.");
            courseR.deleteById(id);

    }

}
