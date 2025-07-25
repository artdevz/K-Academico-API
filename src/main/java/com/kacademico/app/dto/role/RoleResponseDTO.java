package com.kacademico.app.dto.role;

import java.util.UUID;

public record RoleResponseDTO(
    UUID id,
    String name,
    String description
) {}
