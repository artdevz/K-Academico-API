package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Equivalence;
import com.kacademico.infra.entities.EquivalenceEntity;

public class EquivalenceEntityMapper {

    public static Equivalence toDomain(EquivalenceEntity entity, boolean details) {
        if (entity == null) return null;
        Equivalence equivalence = new Equivalence(
            entity.getId(),
            entity.getName()            
        );

        if (details)
            entity.getSubjects().stream().map(subject -> SubjectEntityMapper.toDomain(subject, false)).toList();

        return equivalence;
    }

    public static EquivalenceEntity toEntity(Equivalence equivalence) {
        if (equivalence == null) return null;
        EquivalenceEntity entity = new EquivalenceEntity();
        entity.setId(equivalence.getId());
        entity.setName(equivalence.getName());
        equivalence.getSubjects().stream().map(SubjectEntityMapper::toEntity).toList();

        return entity;
    }
    
}
