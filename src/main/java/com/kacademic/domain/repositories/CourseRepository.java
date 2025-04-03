package com.kacademic.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.domain.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    
}
