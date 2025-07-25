package com.kacademico.infra.mapper;

import java.util.stream.Collectors;

import com.kacademico.domain.models.Course;
import com.kacademico.infra.embeddables.WorkloadEmbeddable;
import com.kacademico.infra.entities.CourseEntity;

public class CourseEntityMapper {

    public static Course toDomain(CourseEntity entity, boolean details) {
        if (entity == null) return null;
        Course course = new Course(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.getDescription(),
            entity.getWorkload().toDomain()
        );

        if (details) {
            course.getStudents().addAll(entity.getStudents().stream().map(student -> StudentEntityMapper.toDomain(student, false)).collect(Collectors.toList()));
            course.getSubjects().addAll(entity.getSubjects().stream().map(subject -> SubjectEntityMapper.toDomain(subject, false)).collect(Collectors.toList()));  
        } 
              
        return course;
    }

    public static CourseEntity toEntity(Course course) {
        if (course == null) return null;
        CourseEntity entity = new CourseEntity();
        entity.setId(course.getId());
        entity.setName(course.getName());
        entity.setCode(course.getCode());
        entity.setDescription(course.getDescription());
        entity.setWorkload(WorkloadEmbeddable.fromDomain(course.getWorkload()));

        return entity;
    }

}
