package com.kacademic.app.dto.subject;

import java.util.List;
import java.util.UUID;

public record SubjectResponseDTO(
    UUID id,
    UUID course,
    String name,
    String description,
    int duration,
    int semester,
    List<UUID> prerequisites
) {}
