package com.kacademico.infra.mapper;

import com.kacademico.domain.models.Token;
import com.kacademico.infra.entities.TokenEntity;

public class TokenEntityMapper {
    
    public static Token toDomain(TokenEntity entity) {
        if (entity == null) return null;
        Token token = new Token(
            entity.getId(),
            entity.getToken(),
            entity.getExpiryDate(),
            UserEntityMapper.toDomain(entity.getUser())
        );

        return token;
    }

    public static TokenEntity toEntity(Token token) {
        if (token == null) return null;
        TokenEntity entity = new TokenEntity();
        entity.setId(token.getId());
        entity.setToken(token.getToken());
        entity.setExpiryDate(token.getExpiryDate());
        entity.setUser(UserEntityMapper.toEntity(token.getUser()));
        
        return entity;
    }

}
