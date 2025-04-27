package com.kacademico.app.dto.course;

import java.util.UUID;

public record CourseResponseDTO(
    UUID id,
    String name,
    String code,
    int duration,
    String description
) {}
