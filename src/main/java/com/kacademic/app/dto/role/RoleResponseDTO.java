package com.kacademic.app.dto.role;

import java.util.UUID;

public record RoleResponseDTO(
    UUID id,
    String name,
    String description
) {}
