package com.kacademico.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {

    boolean existsByEnrolleeIdAndExamId(UUID enrolleeId, UUID examId);

}
