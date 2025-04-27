package com.kacademico.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Equivalence;

@Repository
public interface EquivalenceRepository extends JpaRepository<Equivalence, UUID> {

    @EntityGraph(attributePaths = {"subjects"})
    Optional<Equivalence> findById(UUID id);

}
