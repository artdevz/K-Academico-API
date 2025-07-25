package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Enrollee;
import com.kacademico.infra.entities.EnrolleeEntity;

public class EnrolleEntityMapper {
    
    public static Enrollee toDomain(EnrolleeEntity entity, boolean details) {
        if (entity == null) return null;
        Enrollee enrollee = new Enrollee(
            entity.getId(),
            entity.getAbsences(),
            entity.getAverage(),
            entity.getStatus(),
            GradeEntityMapper.toDomain(entity.getGrade(), false),
            StudentEntityMapper.toDomain(entity.getStudent(), false)
        );

        if (details) {
            enrollee.getEvaluations().addAll(entity.getEvaluations().stream().map(EvaluationEntityMapper::toDomain).toList());
            enrollee.getAttendances().addAll(entity.getAttendances().stream().map(AttendanceEntityMapper::toDomain).toList());
        }

        return enrollee;
    }

    public static EnrolleeEntity toEntity(Enrollee enrollee) {
        if (enrollee == null) return null;
        EnrolleeEntity entity = new EnrolleeEntity();
        entity.setId(enrollee.getId());
        entity.setAbsences(enrollee.getAbsences());
        entity.setAverage(enrollee.getAverage());
        entity.setStatus(enrollee.getStatus());
        entity.setGrade(GradeEntityMapper.toEntity(enrollee.getGrade()));
        entity.setStudent(StudentEntityMapper.toEntity(enrollee.getStudent()));

        return entity;
    }

}
