package com.kacademico.app.dto.subject;

import java.util.UUID;

public record SubjectResponseDTO(
    UUID id,
    UUID course,
    String name,
    String description,
    int duration,
    int semester,
    boolean isRequired
) {}
