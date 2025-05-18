package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.Grade;

public interface IGradeRepository {

    List<Grade> findAll();
    Optional<Grade> findById(UUID id);
    Grade save(Grade grade);
    void deleteById(UUID id);

    Optional<Grade> findWithEnrolleesById(UUID id);
    List<Grade> findByScheduleSemesterAndStatus(String semester, EGrade status);

}
