package com.kacademico.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.kacademico.configs.jwt.JwtService;
import com.kacademico.models.User;

@Service
public class LoginService {
    
    private final LoginRepository loginR;

    private final JwtService jwtS;

    private final AuthenticationManager authManager;

    public LoginService(LoginRepository loginR, JwtService jwtS, AuthenticationManager authManager) {
        this.loginR = loginR;
        this.jwtS = jwtS;
        this.authManager = authManager;
    }

    public String login(LoginRequestDTO data) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(data.email(), data.password()));
        User user = loginR.findByEmail(data.email()).get();

        return jwtS.generateToken(user);

    }

}
