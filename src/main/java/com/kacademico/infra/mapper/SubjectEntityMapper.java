package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Subject;
import com.kacademico.infra.entities.SubjectEntity;

public class SubjectEntityMapper {
    
    public static Subject toDomain(SubjectEntity entity, boolean details) {
        if (entity == null) return null;
        Subject subject = new Subject(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getDuration(),
            entity.getSemester(),
            entity.isRequired(),
            CourseEntityMapper.toDomain(entity.getCourse(), false)
        );

        if (details) {
            subject.getGrades().addAll(entity.getGrades().stream().map(grade -> GradeEntityMapper.toDomain(grade, details)).toList());
            subject.getPrerequisites().addAll(entity.getPrerequisites().stream().map(prerequisites -> EquivalenceEntityMapper.toDomain(prerequisites, false)).toList());
        }

        return subject;
    }

    public static SubjectEntity toEntity(Subject subject) {
        if (subject == null) return null;
        SubjectEntity entity = new SubjectEntity();
        entity.setId(subject.getId());
        entity.setName(subject.getName());
        entity.setDescription(subject.getDescription());
        entity.setDuration(subject.getDuration());
        entity.setSemester(subject.getSemester());
        entity.setRequired(subject.isRequired());
        entity.setCourse(CourseEntityMapper.toEntity(subject.getCourse()));

        return entity;
    }

}
