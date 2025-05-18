package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.repositories.IGradeRepository;
import com.kacademico.infra.repositories.jpa.GradeJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class GradeRepository implements IGradeRepository {
    
    private final GradeJpaRepository jpa;

    @Override
    public List<Grade> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Grade> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Grade save(Grade grade) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Grade> findWithEnrolleesById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findWithEnrolleesById'");
    }

    @Override
    public List<Grade> findByScheduleSemesterAndStatus(String semester, EGrade status) {
        throw new UnsupportedOperationException("Unimplemented method 'findByScheduleSemesterAndStatus'");
    }

}
