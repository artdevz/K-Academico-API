package com.kacademic.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollees")
    List<Student> findAllWithEnrollees();

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.enrollees WHERE s.id = :id")
    Optional<Student> findByIdWithEnrollees(@Param("id") UUID id);

}
