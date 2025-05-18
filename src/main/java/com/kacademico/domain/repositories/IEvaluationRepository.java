package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Evaluation;

public interface IEvaluationRepository {

    List<Evaluation> findAll();
    Optional<Evaluation> findById(UUID id);
    Evaluation save(Evaluation evaluation);
    void deleteById(UUID id);

    boolean existsByEnrolleeIdAndExamId(UUID enrolleeId, UUID examId);

}
