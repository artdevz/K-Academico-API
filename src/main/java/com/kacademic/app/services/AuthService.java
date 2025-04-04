package com.kacademic.app.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.auth.AuthRequestDTO;
import com.kacademic.infra.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> login(AuthRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.email(), data.password()));
    
            return jwtProvider.generateToken(authentication);
        }, taskExecutor);
    }

}