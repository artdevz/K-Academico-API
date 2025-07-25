package com.kacademico.infra.mapper;

import java.util.stream.Collectors;

import com.kacademico.domain.models.Professor;
import com.kacademico.infra.entities.ProfessorEntity;

public class ProfessorEntityMapper {

    public static Professor toDomain(ProfessorEntity entity, boolean details) {
        if (entity == null) return null;
        Professor professor = new Professor(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRoles().stream().map(RoleEntityMapper::toDomain).collect(Collectors.toSet())
        );

        if (details)
            professor.getGrades().addAll(entity.getGrades().stream().map(grade -> GradeEntityMapper.toDomain(grade, false)).toList());

        return professor;
    }

    public static ProfessorEntity toEntity(Professor professor) {
        if (professor == null) return null;
        ProfessorEntity entity = new ProfessorEntity();
        entity.setId(professor.getId());
        entity.setName(professor.getName());
        entity.setEmail(professor.getEmail());
        entity.setPassword(professor.getPassword());
        entity.setRoles(professor.getRoles().stream().map(RoleEntityMapper::toEntity).collect(Collectors.toSet()));

        return entity;
    }
    
}
