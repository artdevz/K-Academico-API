package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Token;
import com.kacademico.domain.repositories.ITokenRepository;
import com.kacademico.infra.entities.TokenEntity;
import com.kacademico.infra.mapper.TokenEntityMapper;
import com.kacademico.infra.repositories.jpa.TokenJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TokenRepository implements ITokenRepository {

    private final TokenJpaRepository jpa;

    @Override
    public List<Token> findAll() {
        return jpa.findAll().stream().map(TokenEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Token> findById(UUID id) {
        return jpa.findById(id).map(TokenEntityMapper::toDomain);
    }

    @Override
    public Token save(Token token) {
        TokenEntity entity = TokenEntityMapper.toEntity(token);
        TokenEntity saved = jpa.save(entity);
        
        return TokenEntityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return jpa.findByToken(token).map(TokenEntityMapper::toDomain);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }
    
}
