package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Equivalence;

public interface IEquivalenceRepository {

    List<Equivalence> findAll();
    Optional<Equivalence> findById(UUID id);
    Equivalence save(Equivalence equivalence);
    void deleteById(UUID id);

}
