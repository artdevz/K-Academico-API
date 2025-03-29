package com.kacademic.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    
}
