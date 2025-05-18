package com.kacademico.infra.repositories.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.ProfessorEntity;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, UUID> {}
