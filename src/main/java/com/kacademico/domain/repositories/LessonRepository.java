package com.kacademico.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @Query("SELECT l FROM Lesson l JOIN FETCH l.grade WHERE l.id = :id")
    Optional<Lesson> findByIdWithGrade(@Param("id") UUID id);

}
