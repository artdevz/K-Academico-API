package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kacademico.infra.entities.LessonEntity;

public interface LessonJpaRepository extends JpaRepository<LessonEntity, UUID> {
    
    @Query("SELECT l FROM Lesson l JOIN FETCH l.grade WHERE l.id = :id")
    Optional<LessonEntity> findWithGradeById(@Param("id") UUID id);

}
