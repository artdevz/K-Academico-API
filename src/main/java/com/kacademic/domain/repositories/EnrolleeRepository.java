package com.kacademic.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Enrollee;

@Repository
public interface EnrolleeRepository extends JpaRepository<Enrollee, UUID> {
    
    @Query("SELECT e FROM Enrollee e LEFT JOIN FETCH e.evaluations LEFT JOIN FETCH e.attendances WHERE e.id = :id")
    Optional<Enrollee> findByIdWithEvaluationsAndAttendances(@Param("id") UUID id);    

    @Modifying
    @Query("UPDATE Enrollee e SET e.grade = NULL WHERE e.grade.id = :gradeId")
    void removeGradeFromEnrollees(@Param("gradeId") UUID gradeId);

}
