package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Enrollee;

public interface IEnrolleeRepository {

    List<Enrollee> findAll();
    Optional<Enrollee> findById(UUID id);
    Enrollee save(Enrollee enrollee);
    void deleteById(UUID id);
    
    Optional<Enrollee> findByIdWithEvaluationsAndAttendances(UUID id);    
    void removeGradeFromEnrollees(UUID gradeId);

}
