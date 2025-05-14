package com.kacademico.infra.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.infra.entities.SubjectEntity;

@Repository
public interface JpaSubjectRepository extends JpaRepository<SubjectEntity, UUID> {
    
    @EntityGraph(attributePaths = {"course", "prerequisites", "prerequisites.subjects"})
    Optional<SubjectEntity> findById(UUID id);

}
