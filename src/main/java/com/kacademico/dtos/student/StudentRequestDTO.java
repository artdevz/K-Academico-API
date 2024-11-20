package com.kacademico.dtos.student;

import java.util.UUID;

import com.kacademico.enums.EShift;

public record StudentRequestDTO(UUID user, UUID course, EShift shift) {}
