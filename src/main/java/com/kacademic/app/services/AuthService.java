package com.kacademic.app.services;

// import java.util.concurrent.CompletableFuture;

// import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.auth.AuthRequestDTO;
import com.kacademic.infra.security.JwtTokenProvider;

@Service
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // @Async
    public String login(AuthRequestDTO data) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.email(), data.password()));

        // return CompletableFuture.completedFuture(jwtTokenProvider.generateToken(authentication));
        return jwtTokenProvider.generateToken(authentication);

    }

}
