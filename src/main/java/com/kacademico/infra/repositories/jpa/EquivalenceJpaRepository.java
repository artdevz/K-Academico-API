package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.EquivalenceEntity;

public interface EquivalenceJpaRepository extends JpaRepository<EquivalenceEntity, UUID> {
    
    @EntityGraph(attributePaths = {"subjects"})
    Optional<EquivalenceEntity> findById(UUID id);

}
