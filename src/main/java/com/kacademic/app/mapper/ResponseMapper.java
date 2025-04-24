package com.kacademic.app.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kacademic.app.dto.course.CourseResponseDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Subject;

@Component
public class ResponseMapper {
    
    public CourseResponseDTO toCourseResponseDTO(Course course) {
        return new CourseResponseDTO(
            course.getId(),
            course.getName(),
            course.getCode(),
            course.getDuration(),
            course.getDescription()
        );
    }

    public List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courses) {
        return courses.stream()
            .map(this::toCourseResponseDTO)
            .collect(Collectors.toList());
    }

    public SubjectResponseDTO toSubjectResponseDTO(Subject subject) {
        return new SubjectResponseDTO(
            subject.getId(),
            subject.getCourse().getId(),
            subject.getName(),
            subject.getDescription(),
            subject.getDuration(),
            subject.getSemester(),
            subject.isRequired()
        );
    }

    public List<SubjectResponseDTO> toSubjectResponseDTOList(List<Subject> subjects) {
        return subjects.stream()
            .map(this::toSubjectResponseDTO)
            .collect(Collectors.toList());
    }

}
