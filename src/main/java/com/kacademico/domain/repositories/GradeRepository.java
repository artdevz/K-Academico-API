package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;
import com.kacademico.shared.utils.Semester;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {

    @EntityGraph(attributePaths = {"enrollees"})
    Optional<Grade> findWithEnrolleesById(UUID id);

    @Query("SELECT g FROM Grade g WHERE g.semester = :semester AND g.status = :status")
    List<Grade> findAllBySemesterAndStatus(@Param("semester") @Semester String semester, @Param("status") EGrade status);

    @Query("SELECT DISTINCT g FROM Grade g LEFT JOIN FETCH g.enrollees WHERE g.semester = :semester AND g.status = :status")
    List<Grade> findAllWithEnrolleesBySemesterAndStatus(@Param("semester") @Semester String semester, @Param("status") EGrade status);

    @EntityGraph(attributePaths = {"timetables"})
    Optional<Grade> findById(UUID id);

    @EntityGraph(attributePaths = {"timetables"})
    List<Grade> findAll();

}
