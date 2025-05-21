package com.kacademico.infra.repositories.jpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kacademico.infra.entities.StudentEntity;

public interface StudentJpaRepository extends JpaRepository<StudentEntity, UUID> {
    
    @Query(value = "SELECT s.enrollment.value FROM Student s WHERE s.enrollment.value LIKE CONCAT(:prefix, '%')", nativeQuery = true)
    Set<String> findAllEnrollmentsByPrefix(@Param("prefix") String prefix);

    @EntityGraph(attributePaths = {"enrollees"})
    List<StudentEntity> findAll();

    @EntityGraph(attributePaths = {"enrollees"})
    Optional<StudentEntity> findWithEnrolleesById(@Param("id") UUID id);

}
