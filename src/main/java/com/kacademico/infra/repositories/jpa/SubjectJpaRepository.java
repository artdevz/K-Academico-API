package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.SubjectEntity;

public interface SubjectJpaRepository extends JpaRepository<SubjectEntity, UUID> {
    
    @EntityGraph(attributePaths = {"course", "prerequisites", "prerequisites.subjects", "grades"})
    Optional<SubjectEntity> findById(UUID id);

}
