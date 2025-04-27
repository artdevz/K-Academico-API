package com.kacademico.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, UUID> {}
