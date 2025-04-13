package com.kacademic.app.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.domain.models.Token;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.TokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {
    
    private final TokenRepository tokenR;

    public Token createRefreshToken(User user) {
        Token token = new Token(
            user,
            UUID.randomUUID().toString(),
            new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)
        );
           
        return tokenR.save(token);
    }

    public Token validateRefreshToken(String token) {
        return tokenR.findByToken(token)
            .filter(t -> t.getExpiryDate().after(new Date())).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token is Invalid or Expired"));
    }

    @Transactional
    public void deleteByUserId(UUID id) {
        tokenR.deleteByUserId(id);
    }

}
