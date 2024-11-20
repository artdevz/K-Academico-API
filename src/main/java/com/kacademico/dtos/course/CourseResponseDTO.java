package com.kacademico.dtos.course;

import java.util.UUID;

public record CourseResponseDTO(UUID id, String name, int duration, String description) {}
