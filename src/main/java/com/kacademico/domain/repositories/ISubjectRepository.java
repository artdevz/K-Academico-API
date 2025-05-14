package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Subject;

public interface ISubjectRepository {

    Optional<Subject> findById(UUID id);
    List<Subject> findAll();
    Subject save(Subject subject);
    void deleteById(UUID id);    

}
