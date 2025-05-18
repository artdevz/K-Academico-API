package com.kacademico.infra.mapper;

import java.util.stream.Collectors;

import com.kacademico.domain.models.Student;
import com.kacademico.infra.entities.StudentEntity;

public class StudentEntityMapper {
    
    public static Student toDomain(StudentEntity entity, boolean details) {
        if (entity == null) return null;
        Student student = new Student(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRoles().stream().map(RoleEntityMapper::toDomain).collect(Collectors.toSet()),
            entity.getCredits(),
            entity.getAverage(),
            entity.getEnrollment(),
            CourseEntityMapper.toDomain(entity.getCourse(), false)
        );

        if (details) if (entity.getEnrollees() != null);

        return student;
    }

    public static StudentEntity toEntity(Student student) {
        if (student == null) return null;
        StudentEntity entity = new StudentEntity();
        entity.setId(entity.getId());
        entity.setName(entity.getName());
        entity.setEmail(entity.getEmail());
        entity.setPassword(entity.getPassword());
        entity.setRoles(entity.getRoles());
        entity.setCredits(entity.getCredits());
        entity.setAverage(entity.getAverage());
        entity.setEnrollment(entity.getEnrollment());

        return entity;
    }

}
