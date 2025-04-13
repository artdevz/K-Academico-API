package com.kacademic.app.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.auth.AuthRefreshDTO;
import com.kacademic.app.dto.auth.AuthRequestDTO;
import com.kacademic.app.dto.auth.AuthResponseDTO;
import com.kacademic.domain.models.Token;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.UserRepository;
import com.kacademic.infra.security.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
    
    private final UserRepository userR;
    private final JwtService jwtS;
    private final TokenService tokenS;
    private final AuthenticationManager authenticationManager;
    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<AuthResponseDTO> loginAsync(AuthRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.email(), data.password()));
    
            User user = findUserByEmail(auth.getName());

            return new AuthResponseDTO(
                jwtS.generateAccessToken(user),
                tokenS.createRefreshToken(user).getToken()
            );
        }, taskExecutor);
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