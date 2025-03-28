package com.kacademic.dto.user;

import java.util.Set;
import java.util.UUID;

import com.kacademic.models.AppRole;

public record UserResponseDTO(UUID id, String name, String email, Set<AppRole> roles) {}
