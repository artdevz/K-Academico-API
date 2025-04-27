package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("SELECT s.enrollment.value FROM Student s WHERE s.enrollment.value LIKE CONCAT(:prefix, '%')")
    Set<String> findAllEnrollmentsByPrefix(@Param("prefix") String prefix);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollees")
    List<Student> findAllWithEnrollees();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollees WHERE s.id = :id")
    Optional<Student> findByIdWithEnrollees(@Param("id") UUID id);

}
