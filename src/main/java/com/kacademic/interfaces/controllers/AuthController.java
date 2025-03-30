package com.kacademic.interfaces.controllers;

// import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.dto.auth.AuthRequestDTO;
import com.kacademic.app.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {
    
    private final AuthService authS;

    public AuthController(AuthService authS) {
        this.authS = authS;
    }

    @Operation(
        summary = "User login",
        description = "Authenticates a user by validating the provided credentials. <br>If successful, returns an authentication token. Otherwise, returns an error message."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful. Authentication token returned."),
        @ApiResponse(responseCode = "400", description = "Bad Request: Invalid input or request format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthRequestDTO request) {
        // return authS.login(request).thenApply(token -> ResponseEntity.ok(token));
        return new ResponseEntity<>(authS.login(request), HttpStatus.OK);
    }

}
