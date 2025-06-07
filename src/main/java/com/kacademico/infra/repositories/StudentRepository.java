package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Student;
import com.kacademico.domain.repositories.IStudentRepository;
import com.kacademico.infra.entities.StudentEntity;
import com.kacademico.infra.mapper.StudentEntityMapper;
import com.kacademico.infra.repositories.jpa.StudentJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class StudentRepository implements IStudentRepository {
    
    private final StudentJpaRepository jpa;

    @Override
    public List<Student> findAll() {
        return jpa.findAll().stream().map(entity -> StudentEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return jpa.findById(id).map(entity -> StudentEntityMapper.toDomain(entity, true));
    }

    @Override
    public Student save(Student student) {
        StudentEntity entity = StudentEntityMapper.toEntity(student);
        StudentEntity saved = jpa.save(entity);
        
        return StudentEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Set<String> findAllEnrollmentsByPrefix(String prefix) {
        return jpa.findAllEnrollmentsByPrefix(prefix);
    }

}
