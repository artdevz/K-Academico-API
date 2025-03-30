package com.kacademic.app.dto.auth;

import jakarta.validation.constraints.Email;

public record AuthRequestDTO(

    @Email
    String email,
    
    String password

) {}
