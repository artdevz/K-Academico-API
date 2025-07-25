package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Evaluation;
import com.kacademico.infra.entities.EvaluationEntity;

public class EvaluationEntityMapper {
    
    public static Evaluation toDomain(EvaluationEntity entity) {
        if (entity == null) return null;
        Evaluation evaluation = new Evaluation(
            entity.getId(),
            entity.getScore(),
            EnrolleEntityMapper.toDomain(entity.getEnrollee(), false),
            ExamEntityMapper.toDomain(entity.getExam(), false)
        );

        return evaluation;
    }

    public static EvaluationEntity toEntity(Evaluation evaluation) {
        if (evaluation == null) return null;
        EvaluationEntity entity = new EvaluationEntity();
        entity.setId(evaluation.getId());
        entity.setScore(evaluation.getScore());
        entity.setEnrollee(EnrolleEntityMapper.toEntity(evaluation.getEnrollee()));
        entity.setExam(ExamEntityMapper.toEntity(evaluation.getExam()));

        return entity;
    }

}
