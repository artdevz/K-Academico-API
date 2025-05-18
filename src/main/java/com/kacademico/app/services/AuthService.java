package com.kacademico.app.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.auth.AuthRefreshDTO;
import com.kacademico.app.dto.auth.AuthRequestDTO;
import com.kacademico.app.dto.auth.AuthResponseDTO;
import com.kacademico.domain.models.Token;
import com.kacademico.domain.models.User;
import com.kacademico.domain.repositories.IUserRepository;
import com.kacademico.infra.security.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
    
    private final IUserRepository userR;
    private final JwtService jwtS;
    private final TokenService tokenS;
    private final AuthenticationManager authenticationManager;

    @Async
    public CompletableFuture<AuthResponseDTO> loginAsync(AuthRequestDTO data) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.email(), data.password()));

        User user = findUserByEmail(auth.getName());

        return CompletableFuture.completedFuture(
            new AuthResponseDTO(
                jwtS.generateAccessToken(user),
                tokenS.createRefreshToken(user).getToken()
            )
        );
    }

    public AuthResponseDTO refreshSync(AuthRefreshDTO data) {
        Token token = tokenS.validateRefreshToken(data.refreshToken());
        User user = token.getUser();

        tokenS.deleteByUserId(user.getId());            

        return new AuthResponseDTO(
            jwtS.generateAccessToken(user),
            tokenS.createRefreshToken(user).getToken()
        );
    }

    private User findUserByEmail(String email) {
        return userR.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
    }

}