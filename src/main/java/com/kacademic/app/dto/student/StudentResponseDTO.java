package com.kacademic.app.dto.student;

import java.util.UUID;

public record StudentResponseDTO(UUID id, UUID course, String enrollment, String name, String email, float average) {}
