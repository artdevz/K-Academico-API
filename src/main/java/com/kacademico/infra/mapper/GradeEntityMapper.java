package com.kacademico.infra.mapper;

import java.util.stream.Collectors;

import com.kacademico.domain.models.Grade;
import com.kacademico.infra.embeddables.ScheduleEmbeddable;
import com.kacademico.infra.embeddables.TimetableEmbeddable;
import com.kacademico.infra.entities.GradeEntity;

public class GradeEntityMapper {
    
    public static Grade toDomain(GradeEntity entity, boolean details) {
        if (entity == null) return null;
        Grade grade = new Grade(
            entity.getId(),
            entity.getCapacity(),
            entity.getCurrentStudents(),
            entity.getStatus(),
            entity.getSchedule().toDomain(),
            entity.getTimetables().stream().map(TimetableEmbeddable::toDomain).toList(),
            SubjectEntityMapper.toDomain(entity.getSubject(), false),
            ProfessorEntityMapper.toDomain(entity.getProfessor(), false)
        );

        if (details) {
            grade.getEnrollees().addAll(entity.getEnrollees().stream().map(enrollee -> EnrolleEntityMapper.toDomain(enrollee, false)).collect(Collectors.toSet()));
            grade.getExams().addAll(entity.getExams().stream().map(exam -> ExamEntityMapper.toDomain(exam, false)).toList());
            grade.getLessons().addAll(entity.getLessons().stream().map(lesson -> LessonEntityMapper.toDomain(lesson, false)).toList());
        }

        return grade;
    }

    public static GradeEntity toEntity(Grade grade) {
        if (grade == null) return null;
        GradeEntity entity = new GradeEntity();
        entity.setId(grade.getId());
        entity.setCapacity(grade.getCapacity());
        entity.setCurrentStudents(grade.getCurrentStudents());
        entity.setStatus(grade.getStatus());
        entity.setSchedule(ScheduleEmbeddable.fromDomain(grade.getSchedule()));
        entity.setTimetables(grade.getTimetables().stream().map(TimetableEmbeddable::fromDomain).toList());
        entity.setSubject(SubjectEntityMapper.toEntity(grade.getSubject()));
        entity.setProfessor(ProfessorEntityMapper.toEntity(grade.getProfessor()));

        return entity;
    }

}
