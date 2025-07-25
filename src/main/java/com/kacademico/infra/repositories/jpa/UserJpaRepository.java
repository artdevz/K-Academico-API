package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    
    @EntityGraph(attributePaths = {"roles"})
    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByEmail(String email);

}
