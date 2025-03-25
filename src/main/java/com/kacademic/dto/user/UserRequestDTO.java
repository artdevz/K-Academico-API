package com.kacademic.dto.user;

import com.kacademic.utils.Password;

import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    @Size(min=3, max=48, message="Name must be between 3 and 48 characters")
    String name,
    String email,
    @Password String password
) {}
