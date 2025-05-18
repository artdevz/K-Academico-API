package com.kacademico.infra.repositories.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.TokenEntity;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, UUID> {
    
    Optional<TokenEntity> findByToken(String token);
    void deleteByUserId(UUID userId);

}
