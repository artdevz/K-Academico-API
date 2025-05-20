package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Exam;
import com.kacademico.domain.repositories.IExamRepository;
import com.kacademico.infra.entities.ExamEntity;
import com.kacademico.infra.mapper.ExamEntityMapper;
import com.kacademico.infra.repositories.jpa.ExamJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ExamRepository implements IExamRepository {
    
    private final ExamJpaRepository jpa;

    @Override
    public List<Exam> findAll() {
        return jpa.findAll().stream().map(entity -> ExamEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Exam> findById(UUID id) {
        return jpa.findById(id).map(entity -> ExamEntityMapper.toDomain(entity, true));
    }

    @Override
    public Exam save(Exam exam) {
        ExamEntity entity = ExamEntityMapper.toEntity(exam);
        ExamEntity saved = jpa.save(entity);
        
        return ExamEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Exam> findWithGradeById(UUID id) {
        return jpa.findWithGradeById(id).map(entity -> ExamEntityMapper.toDomain(entity, true));
    }

}
