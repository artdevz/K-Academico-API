package com.kacademico.app.dto.course;

import java.util.UUID;

import com.kacademico.domain.models.values.Workload;

public record CourseResponseDTO(
    UUID id,
    String name,
    String code,
    String description,
    Workload workload
) {}
