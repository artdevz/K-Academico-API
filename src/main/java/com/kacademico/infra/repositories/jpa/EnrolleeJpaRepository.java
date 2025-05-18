package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kacademico.infra.entities.EnrolleeEntity;

public interface EnrolleeJpaRepository extends JpaRepository<EnrolleeEntity, UUID> {
    
    @Query("SELECT e FROM Enrollee e LEFT JOIN FETCH e.evaluations LEFT JOIN FETCH e.attendances WHERE e.id = :id")
    Optional<EnrolleeEntity> findByIdWithEvaluationsAndAttendances(@Param("id") UUID id);    

    @Modifying
    @Query("UPDATE Enrollee e SET e.grade = NULL WHERE e.grade.id = :gradeId")
    void removeGradeFromEnrollees(@Param("gradeId") UUID gradeId);

}
