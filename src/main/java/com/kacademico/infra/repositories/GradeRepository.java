package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.repositories.IGradeRepository;
import com.kacademico.infra.entities.GradeEntity;
import com.kacademico.infra.mapper.GradeEntityMapper;
import com.kacademico.infra.repositories.jpa.GradeJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class GradeRepository implements IGradeRepository {
    
    private final GradeJpaRepository jpa;

    @Override
    public List<Grade> findAll() {
        return jpa.findAll().stream().map(entity -> GradeEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Grade> findById(UUID id) {
        return jpa.findById(id).map(entity -> GradeEntityMapper.toDomain(entity, true));
    }

    @Override
    public Grade save(Grade grade) {
        GradeEntity entity = GradeEntityMapper.toEntity(grade);
        GradeEntity saved = jpa.save(entity);
        
        return GradeEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Grade> findWithEnrolleesById(UUID id) {
        return jpa.findWithEnrolleesById(id).map(entity -> GradeEntityMapper.toDomain(entity, true));
    }

    @Override
    public List<Grade> findByScheduleSemesterAndStatus(String semester, EGrade status) {
        return jpa.findByScheduleSemesterAndStatus(semester, status).stream().map(entity -> GradeEntityMapper.toDomain(entity, true)).toList();
    }

}
