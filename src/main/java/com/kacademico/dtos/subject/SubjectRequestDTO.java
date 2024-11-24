package com.kacademico.dtos.subject;

import java.util.List;
import java.util.UUID;

public record SubjectRequestDTO(UUID course, String name, String description, int duration, int semester, List<UUID> prerequisites) {}
