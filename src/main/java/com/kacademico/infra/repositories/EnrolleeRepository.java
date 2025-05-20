package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.infra.entities.EnrolleeEntity;
import com.kacademico.infra.mapper.EnrolleEntityMapper;
import com.kacademico.infra.repositories.jpa.EnrolleeJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EnrolleeRepository implements IEnrolleeRepository {
    
    EnrolleeJpaRepository jpa;

    @Override
    public List<Enrollee> findAll() {
        return jpa.findAll().stream().map(entity -> EnrolleEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Enrollee> findById(UUID id) {
        return jpa.findById(id).map(entity -> EnrolleEntityMapper.toDomain(entity, true));
    }

    @Override
    public Enrollee save(Enrollee enrollee) {
        EnrolleeEntity entity = EnrolleEntityMapper.toEntity(enrollee);
        EnrolleeEntity saved = jpa.save(entity);
        
        return EnrolleEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Enrollee> findWithEvaluationsAndAttendancesById(UUID id) {
        return jpa.findWithEvaluationsAndAttendancesById(id).map(entity -> EnrolleEntityMapper.toDomain(entity, true));
    }

    @Override
    public void removeGradeFromEnrollees(UUID gradeId) {
        jpa.removeGradeFromEnrollees(gradeId);
    }

}
