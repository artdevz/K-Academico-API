package com.kacademic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.models.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    
}
