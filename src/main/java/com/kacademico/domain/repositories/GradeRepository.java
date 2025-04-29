package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;
import com.kacademico.shared.utils.Semester;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {

    @EntityGraph(attributePaths = {"enrollees"})
    Optional<Grade> findWithEnrolleesById(UUID id);

    @EntityGraph(attributePaths = {"enrollees"})
    List<Grade> findByScheduleSemesterAndStatus(@Semester String semester, EGrade status);

    @EntityGraph(attributePaths = {"timetables"})
    Optional<Grade> findById(UUID id);

    @EntityGraph(attributePaths = {"timetables"})
    List<Grade> findAll();

}
