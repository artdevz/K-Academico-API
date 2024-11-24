package com.kacademico.dtos.subject;

import java.util.List;
import java.util.UUID;

public record SubjectResponseDTO(UUID id, String course, String name, String description, int duration, int semester, List<UUID> prerequisites) {}
