package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Lesson;
import com.kacademico.infra.entities.LessonEntity;

public class LessonEntityMapper {

    public static Lesson toDomain(LessonEntity entity, boolean details) {
        if (entity == null) return null;
        Lesson lesson = new Lesson(
            entity.getId(),
            entity.getTopic(),
            entity.getDate(),
            entity.getStatus(),
            GradeEntityMapper.toDomain(entity.getGrade(), false)
        );

        if (details)
            lesson.getAttendances().addAll(entity.getAttendances().stream().map(AttendanceEntityMapper::toDomain).toList());

        return lesson;
    }

    public static LessonEntity toEntity(Lesson lesson) {
        if (lesson == null) return null;
        LessonEntity entity = new LessonEntity();
        entity.setId(lesson.getId());
        entity.setTopic(lesson.getTopic());
        entity.setDate(lesson.getDate());
        entity.setStatus(lesson.getStatus());
        entity.setGrade(GradeEntityMapper.toEntity(lesson.getGrade()));

        return entity;
    }
    
}
