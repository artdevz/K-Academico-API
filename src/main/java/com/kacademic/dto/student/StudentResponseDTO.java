package com.kacademic.dto.student;

import java.util.UUID;

import com.kacademic.enums.EShift;

public record StudentResponseDTO(UUID id, String course, String enrollment, String name, String email, float avarage, EShift shift) {}
