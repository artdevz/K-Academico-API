package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.CourseEntity;

public interface CourseJpaRepository extends JpaRepository<CourseEntity, UUID> {

    Optional<CourseEntity> findByCode(String code);
    Optional<CourseEntity> findByName(String name);

    @EntityGraph(attributePaths = {"subjects", "students"})
    Optional<CourseEntity> findById(UUID id);

}
