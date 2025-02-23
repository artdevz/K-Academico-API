package com.kacademic.auth;

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

    public String login(AuthRequestDTO data) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.email(), data.password()));

        return jwtTokenProvider.generateToken(authentication);

    }

}
