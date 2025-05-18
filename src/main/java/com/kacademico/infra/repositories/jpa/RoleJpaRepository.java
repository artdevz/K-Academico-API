package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.RoleEntity;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {
    
    Optional<RoleEntity> findByName(String name);

}
