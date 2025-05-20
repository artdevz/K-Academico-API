package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Professor;
import com.kacademico.domain.repositories.IProfessorRepository;
import com.kacademico.infra.entities.ProfessorEntity;
import com.kacademico.infra.mapper.ProfessorEntityMapper;
import com.kacademico.infra.repositories.jpa.ProfessorJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ProfessorRepository implements IProfessorRepository {
    
    private final ProfessorJpaRepository jpa;

    @Override
    public List<Professor> findAll() {
        return jpa.findAll().stream().map(entity -> ProfessorEntityMapper.toDomain(entity, false)).toList();
    }

    @Override
    public Optional<Professor> findById(UUID id) {
        return jpa.findById(id).map(entity -> ProfessorEntityMapper.toDomain(entity, true));
    }

    @Override
    public Professor save(Professor professor) {
        ProfessorEntity entity = ProfessorEntityMapper.toEntity(professor);
        ProfessorEntity saved = jpa.save(entity);
        
        return ProfessorEntityMapper.toDomain(saved, true);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

}
