package com.kacademic.dto.student;

import java.util.UUID;

import com.kacademic.enums.EShift;

public record StudentRequestDTO(UUID user, UUID course, EShift shift) {}
