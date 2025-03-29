package com.kacademic.app.dto.user;

import java.util.Set;
import java.util.UUID;

import com.kacademic.domain.models.Role;

public record UserResponseDTO(UUID id, String name, String email, Set<Role> roles) {}
