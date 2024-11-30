package com.kacademico.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.models.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    
}