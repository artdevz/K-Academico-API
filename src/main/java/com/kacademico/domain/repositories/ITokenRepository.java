package com.kacademico.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.kacademico.domain.models.Token;

public interface ITokenRepository {
    
    List<Token> findAll();
    Optional<Token> findById(UUID id);
    Token save(Token token);
    void deleteById(UUID id);

    Optional<Token> findByToken(String token);
    void deleteByUserId(UUID userId);

}
