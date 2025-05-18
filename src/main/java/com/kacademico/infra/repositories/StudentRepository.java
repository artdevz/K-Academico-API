package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Student;
import com.kacademico.domain.repositories.IStudentRepository;
import com.kacademico.infra.repositories.jpa.StudentJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StudentRepository implements IStudentRepository {
    
    private final StudentJpaRepository jpa;

    @Override
    public List<Student> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Student> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Student save(Student student) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Set<String> findAllEnrollmentsByPrefix(String prefix) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllEnrollmentsByPrefix'");
    }

    @Override
    public List<Student> findAllWithEnrollees() {
        throw new UnsupportedOperationException("Unimplemented method 'findAllWithEnrollees'");
    }

    @Override
    public Optional<Student> findByIdWithEnrollees(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithEnrollees'");
    }

}
