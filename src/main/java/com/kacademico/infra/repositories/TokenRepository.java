package com.kacademico.infra.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kacademico.domain.models.Token;
import com.kacademico.domain.repositories.ITokenRepository;
import com.kacademico.infra.repositories.jpa.TokenJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class TokenRepository implements ITokenRepository {

    private final TokenJpaRepository jpa;

    @Override
    public List<Token> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<Token> findById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Token save(Token token) {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        throw new UnsupportedOperationException("Unimplemented method 'findByToken'");
    }

    @Override
    public void deleteByUserId(UUID userId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteByUserId'");
    }
    
}
