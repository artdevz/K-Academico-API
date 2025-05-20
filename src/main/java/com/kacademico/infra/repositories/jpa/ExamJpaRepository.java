package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kacademico.infra.entities.ExamEntity;

public interface ExamJpaRepository extends JpaRepository<ExamEntity, UUID> {
    
    @Query("SELECT e FROM Exam e JOIN FETCH e.grade WHERE e.id = :id")
    Optional<ExamEntity> findWithGradeById(@Param("id") UUID id);

}
