package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Subject;
import com.kacademico.domain.repositories.ISubjectRepository;
import com.kacademico.infra.entities.SubjectEntity;
import com.kacademico.infra.mapper.SubjectEntityMapper;
import com.kacademico.infra.repositories.jpa.SubjectJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class SubjectRepository implements ISubjectRepository {

    private final SubjectJpaRepository jpa;

    @Override
    public List<Subject> findAll() {
        return jpa.findAll().stream()
            .map(entity -> SubjectEntityMapper.toDomain(entity, false))
            .toList();
    }

	@Override
	public Optional<Subject> findById(UUID id) {
        return jpa.findById(id).map(entity -> SubjectEntityMapper.toDomain(entity, true));
    }

	@Override
	public Subject save(Subject subject) {
        SubjectEntity entity = SubjectEntityMapper.toEntity(subject);
        SubjectEntity saved = jpa.save(entity);
        return SubjectEntityMapper.toDomain(saved, true);
    }

	@Override
	public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
    
}
