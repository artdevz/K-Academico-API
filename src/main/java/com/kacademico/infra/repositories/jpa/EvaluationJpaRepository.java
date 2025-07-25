package com.kacademico.infra.repositories.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.EvaluationEntity;

public interface EvaluationJpaRepository extends JpaRepository<EvaluationEntity, UUID> {
    
    boolean existsByEnrolleeIdAndExamId(UUID enrolleeId, UUID examId);

}
