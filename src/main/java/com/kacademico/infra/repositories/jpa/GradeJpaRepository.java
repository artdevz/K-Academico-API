package com.kacademico.infra.repositories.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.infra.entities.GradeEntity;
import com.kacademico.shared.utils.Semester;

public interface GradeJpaRepository extends JpaRepository<GradeEntity, UUID> {
    
    @EntityGraph(attributePaths = {"enrollees"})
    Optional<GradeEntity> findWithEnrolleesById(UUID id);

    @EntityGraph(attributePaths = {"enrollees"})
    List<GradeEntity> findByScheduleSemesterAndStatus(@Semester String semester, EGrade status);

    @EntityGraph(attributePaths = {"timetables"})
    Optional<GradeEntity> findById(UUID id);

    @EntityGraph(attributePaths = {"timetables"})
    List<GradeEntity> findAll();

}
