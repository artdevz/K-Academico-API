package com.kacademic.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.subjects WHERE c.id = :id")
    Optional<Course> findByIdWithSubjects(@Param("id") UUID id);

}
