package com.kacademico.app.dto.user;

import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.models.Role;

public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    Set<Role> roles
) {}
