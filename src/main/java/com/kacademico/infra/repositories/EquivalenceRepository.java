package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Equivalence;
import com.kacademico.domain.repositories.IEquivalenceRepository;
import com.kacademico.infra.repositories.jpa.EquivalenceJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EquivalenceRepository implements IEquivalenceRepository {
    
    private final EquivalenceJpaRepository jpa;

    @Override
    public List<Equivalence> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Equivalence> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Equivalence save(Equivalence equivalence) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

}
