package com.kacademico.app.dto.enrollee;

import java.util.UUID;

import com.kacademico.domain.enums.EEnrollee;

public record EnrolleeResponseDTO(
    UUID id,
    UUID student,
    UUID grade,
    int absences,
    float avarage,
    EEnrollee status
) {}
