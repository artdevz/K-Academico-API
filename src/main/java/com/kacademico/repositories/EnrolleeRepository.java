package com.kacademico.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademico.models.Enrollee;

@Repository
public interface EnrolleeRepository extends JpaRepository<Enrollee, UUID> {
    
    @Modifying
    @Query("UPDATE Enrollee e SET e.grade = NULL WHERE e.grade.id = :gradeId")
    void removeGradeFromEnrollees(@Param("gradeId") UUID gradeId);

}
