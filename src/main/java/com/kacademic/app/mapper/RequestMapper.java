package com.kacademic.app.mapper;

import org.springframework.stereotype.Component;

import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.domain.models.Course;

@Component
public class RequestMapper {
    
    public Course toCourse(CourseRequestDTO courseRequestDTO) {
        return new Course(
            courseRequestDTO.name(),
            courseRequestDTO.code(),
            courseRequestDTO.description()
        );
    }

}
