package com.kacademico.dtos.student;

import java.util.UUID;

import com.kacademico.enums.EShift;

public record StudentResponseDTO(UUID id, String course, String enrollment, String name, String email, float avarage, EShift shift) {}
