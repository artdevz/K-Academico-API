package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Course;

public interface ICourseRepository {

    List<Course> findAll();
    Optional<Course> findById(UUID id);
    Course save(Course course);
    void deleteById(UUID id);

    Optional<Course> findByCode(String code);
    Optional<Course> findByName(String name);
    Optional<Course> findWithSubjectsById(UUID id);

}
