package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Exam;
import com.kacademico.domain.repositories.IExamRepository;
import com.kacademico.infra.repositories.jpa.ExamJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ExamRepository implements IExamRepository {
    
    private final ExamJpaRepository jpa;

    @Override
    public List<Exam> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Exam> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Exam save(Exam exam) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Exam> findByIdWithGrade(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithGrade'");
    }

}
