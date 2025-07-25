package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Professor;

public interface IProfessorRepository {

    List<Professor> findAll();
    Optional<Professor> findById(UUID id);
    Professor save(Professor professor);
    void deleteById(UUID id);

}
