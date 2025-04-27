package com.kacademico.interfaces.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademico.app.dto.auth.AuthRefreshDTO;
import com.kacademico.app.dto.auth.AuthRequestDTO;
import com.kacademico.app.dto.auth.AuthResponseDTO;
import com.kacademico.app.services.AuthService;
import com.kacademico.app.services.TokenService;
import com.kacademico.domain.models.User;
import com.kacademico.shared.utils.AsyncResultHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authS;
    private final TokenService tokenS;


    @Operation(summary = "User login",
                description = "Authenticates a user by validating the provided credentials. <br>If successful, returns an authentication token. Otherwise, returns an error message.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful. Authentication token returned."),
        @ApiResponse(responseCode = "400", description = "Bad Request: Invalid input or request format"),
        @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(AsyncResultHandler.await(authS.loginAsync(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody @Valid AuthRefreshDTO request) {
        return ResponseEntity.ok(authS.refreshSync(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal User user) {
        tokenS.deleteByUserId(user.getId());
        return ResponseEntity.noContent().build();
    }

}