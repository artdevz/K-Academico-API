package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Evaluation;
import com.kacademico.domain.repositories.IEvaluationRepository;
import com.kacademico.infra.repositories.jpa.EvaluationJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EvaluationRepository implements IEvaluationRepository {
    
    private final EvaluationJpaRepository jpa;

    @Override
    public List<Evaluation> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Evaluation> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByEnrolleeIdAndExamId(UUID enrolleeId, UUID examId) {
        throw new UnsupportedOperationException("Unimplemented method 'existsByEnrolleeIdAndExamId'");
    }

}
