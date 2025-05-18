package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.infra.repositories.jpa.EnrolleeJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EnrolleeRepository implements IEnrolleeRepository {
    
    EnrolleeJpaRepository jpa;

    @Override
    public List<Enrollee> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Enrollee> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Enrollee save(Enrollee enrollee) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Optional<Enrollee> findByIdWithEvaluationsAndAttendances(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithEvaluationsAndAttendances'");
    }

    @Override
    public void removeGradeFromEnrollees(UUID gradeId) {
        throw new UnsupportedOperationException("Unimplemented method 'removeGradeFromEnrollees'");
    }

}
