package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Subject;
import com.kacademico.infra.entities.SubjectEntity;

public class SubjectEntityMapper {
    
    public static Subject toDomain(SubjectEntity entity) {
        if (entity == null) return null;
        Subject subject = new Subject(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getDuration(),
            entity.getSemester(),
            entity.isRequired(),
            CourseEntityMapper.toBaseDomain(entity.getCourse())
        );

        if (entity.getGrades() != null);

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
