package com.kacademic.app.dto.user;

import java.util.Set;
import java.util.UUID;

import com.kacademic.shared.utils.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(

    @Size(min=3, max=48, message="Name must be between 3 and 48 characters")
    String name,

    @Email(message = "Must be a well-formed email address")
    String email,

    @Password 
    String password,

    Set<UUID> roles

) {}