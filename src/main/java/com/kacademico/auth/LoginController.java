package com.kacademico.auth;

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
public class LoginController {
    
    private final LoginService loginS;

    public LoginController(LoginService loginS) {
        this.loginS = loginS;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO data) {

        try {
            return ResponseEntity.ok(loginS.login(data));
        } 
        
        catch (AuthenticationException e) {
            return new ResponseEntity<>("Unathorized", HttpStatus.UNAUTHORIZED);
        }
        
        catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        }

    }

}
