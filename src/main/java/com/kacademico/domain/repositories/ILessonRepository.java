package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Lesson;

public interface ILessonRepository {

    List<Lesson> findAll();
    Optional<Lesson> findById(UUID id);
    Lesson save(Lesson lesson);
    void deleteById(UUID id);

    Optional<Lesson> findByIdWithGrade(UUID id);

}
