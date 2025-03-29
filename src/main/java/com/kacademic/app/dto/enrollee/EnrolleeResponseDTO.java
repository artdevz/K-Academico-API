package com.kacademic.app.dto.enrollee;

import java.util.UUID;

import com.kacademic.domain.enums.EEnrollee;

public record EnrolleeResponseDTO(UUID id, UUID student, UUID grade, int absences, float avarage, EEnrollee status) {}
