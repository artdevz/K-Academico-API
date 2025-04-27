package com.kacademico.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    @Query("SELECT e FROM Exam e JOIN FETCH e.grade WHERE e.id = :id")
    Optional<Exam> findByIdWithGrade(@Param("id") UUID id);

}
