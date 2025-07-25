package com.kacademico.infra.mapper;

import java.util.stream.Collectors;

import com.kacademico.domain.models.Exam;
import com.kacademico.infra.entities.ExamEntity;

public class ExamEntityMapper {
    
    public static Exam toDomain(ExamEntity entity, boolean details) {
        if (entity == null) return null;
        Exam exam = new Exam(
            entity.getId(),
            entity.getName(),
            entity.getMaximum(),
            entity.getDate(),
            GradeEntityMapper.toDomain(entity.getGrade(), false)
        );

        if (details)
            exam.getEvaluations().addAll(entity.getEvaluations().stream().map(EvaluationEntityMapper::toDomain).collect(Collectors.toSet()));

        return exam;
    }

    public static ExamEntity toEntity(Exam exam) {
        if (exam == null) return null;
        ExamEntity entity = new ExamEntity();
        entity.setId(exam.getId());
        entity.setName(exam.getName());
        entity.setMaximum(exam.getMaximum());
        entity.setDate(exam.getDate());
        entity.setGrade(GradeEntityMapper.toEntity(exam.getGrade()));

        return entity;
    }

}
