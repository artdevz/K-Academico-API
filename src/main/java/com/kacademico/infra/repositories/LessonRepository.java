package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.repositories.ILessonRepository;
import com.kacademico.infra.entities.LessonEntity;
import com.kacademico.infra.mapper.LessonEntityMapper;
import com.kacademico.infra.repositories.jpa.LessonJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class LessonRepository implements ILessonRepository {
    
    private final LessonJpaRepository jpa;

    @Override
    public List<Lesson> findAll() {
        return jpa.findAll().stream().map(entity -> LessonEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Lesson> findById(UUID id) {
        return jpa.findById(id).map(entity -> LessonEntityMapper.toDomain(entity, true));
    }

    @Override
    public Lesson save(Lesson lesson) {
        LessonEntity entity = LessonEntityMapper.toEntity(lesson);
        LessonEntity saved = jpa.save(entity);
        
        return LessonEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Lesson> findWithGradeById(UUID id) {
        return jpa.findWithGradeById(id).map(entity -> LessonEntityMapper.toDomain(entity, true));
    }

}
