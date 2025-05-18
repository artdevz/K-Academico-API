package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Exam;

public interface IExamRepository {

    List<Exam> findAll();
    Optional<Exam> findById(UUID id);
    Exam save(Exam exam);
    void deleteById(UUID id);

    Optional<Exam> findByIdWithGrade(UUID id);

}
