package com.kacademic.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {
    
    private final AuthService loginS;

    public AuthController(AuthService loginS) {
        this.loginS = loginS;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthRequestDTO request) {

        try {
            return ResponseEntity.ok(loginS.login(request));
        } 
        
        catch (AuthenticationException e) {
            return new ResponseEntity<>("Unathorized: Invalid Credentials", HttpStatus.UNAUTHORIZED);
        }
        
        catch (Exception e) {
            return new ResponseEntity<>("Bad Request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
