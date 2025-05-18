package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.repositories.ILessonRepository;
import com.kacademico.infra.repositories.jpa.LessonJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class LessonRepository implements ILessonRepository {
    
    private final LessonJpaRepository jpa;

    @Override
    public List<Lesson> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Lesson> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Lesson save(Lesson lesson) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Lesson> findByIdWithGrade(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findByIdWithGrade'");
    }

}
