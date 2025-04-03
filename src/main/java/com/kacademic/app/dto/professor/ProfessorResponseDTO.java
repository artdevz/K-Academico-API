package com.kacademic.app.dto.professor;

import java.util.UUID;

public record ProfessorResponseDTO(
    UUID id,
    String name,
    String email,
    int wage
) {}
