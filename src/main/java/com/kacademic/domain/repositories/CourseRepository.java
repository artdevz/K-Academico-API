package com.kacademic.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @EntityGraph(attributePaths = {"subjects"})
    Optional<Course> findWithSubjectsById(UUID id);

}
