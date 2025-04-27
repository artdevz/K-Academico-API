package com.kacademico.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    @EntityGraph(attributePaths = {"prerequisites", "prerequisites.subjects"})
    Optional<Subject> findById(UUID id);

}
