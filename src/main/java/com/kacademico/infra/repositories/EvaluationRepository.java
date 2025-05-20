package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Evaluation;
import com.kacademico.domain.repositories.IEvaluationRepository;
import com.kacademico.infra.entities.EvaluationEntity;
import com.kacademico.infra.mapper.EvaluationEntityMapper;
import com.kacademico.infra.repositories.jpa.EvaluationJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EvaluationRepository implements IEvaluationRepository {
    
    private final EvaluationJpaRepository jpa;

    @Override
    public List<Evaluation> findAll() {
        return jpa.findAll().stream().map(EvaluationEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Evaluation> findById(UUID id) {
        return jpa.findById(id).map(EvaluationEntityMapper::toDomain);
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        EvaluationEntity entity = EvaluationEntityMapper.toEntity(evaluation);
        EvaluationEntity saved = jpa.save(entity);
        
        return EvaluationEntityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByEnrolleeIdAndExamId(UUID enrolleeId, UUID examId) {
        return jpa.existsByEnrolleeIdAndExamId(enrolleeId, examId);
    }

}
