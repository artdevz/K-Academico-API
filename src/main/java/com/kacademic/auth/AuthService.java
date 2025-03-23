package com.kacademic.auth;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kacademic.security.JwtTokenProvider;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Async
    public CompletableFuture<String> login(AuthRequestDTO data) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.email(), data.password()));

        return CompletableFuture.completedFuture(jwtTokenProvider.generateToken(authentication));

    }

}
