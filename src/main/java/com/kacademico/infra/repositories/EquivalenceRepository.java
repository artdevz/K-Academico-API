package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Equivalence;
import com.kacademico.domain.repositories.IEquivalenceRepository;
import com.kacademico.infra.entities.EquivalenceEntity;
import com.kacademico.infra.mapper.EquivalenceEntityMapper;
import com.kacademico.infra.repositories.jpa.EquivalenceJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class EquivalenceRepository implements IEquivalenceRepository {
    
    private final EquivalenceJpaRepository jpa;

    @Override
    public List<Equivalence> findAll() {
        return jpa.findAll().stream().map(entity -> EquivalenceEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Equivalence> findById(UUID id) {
        return jpa.findById(id).map(entity -> EquivalenceEntityMapper.toDomain(entity, true));
    }

    @Override
    public Equivalence save(Equivalence equivalence) {
        EquivalenceEntity entity = EquivalenceEntityMapper.toEntity(equivalence);
        EquivalenceEntity saved = jpa.save(entity);
        
        return EquivalenceEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

}
