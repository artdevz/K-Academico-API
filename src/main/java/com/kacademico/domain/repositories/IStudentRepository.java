package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.models.Student;

public interface IStudentRepository {

    List<Student> findAll();
    Optional<Student> findById(UUID id);
    Student save(Student student);
    void deleteById(UUID id);

    Set<String> findAllEnrollmentsByPrefix(String prefix);

}
