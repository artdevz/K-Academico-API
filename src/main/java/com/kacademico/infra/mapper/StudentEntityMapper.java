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

        if (details)
            student.getEnrollees().addAll(entity.getEnrollees().stream().map(enrollee -> EnrolleEntityMapper.toDomain(enrollee, false)).toList());

        return student;
    }

    public static StudentEntity toEntity(Student student) {
        if (student == null) return null;
        StudentEntity entity = new StudentEntity();
        entity.setId(student.getId());
        entity.setName(student.getName());
        entity.setEmail(student.getEmail());
        entity.setPassword(student.getPassword());
        entity.setRoles(student.getRoles().stream().map(RoleEntityMapper::toEntity).collect(Collectors.toSet()));
        entity.setCredits(student.getCredits());
        entity.setAverage(student.getAverage());
        entity.setEnrollment(student.getEnrollment());
        entity.setCourse(CourseEntityMapper.toEntity(student.getCourse()));

        return entity;
    }

}
