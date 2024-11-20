package com.kacademico.dtos.student;

import java.util.UUID;

public record StudentResponseDTO(UUID id, String enrollment, String name, String email, String courseName) {}
