package com.kacademic.dto.enrollee;

import java.util.UUID;

import com.kacademic.enums.EEnrollee;

public record EnrolleeResponseDTO(UUID id, String student, String grade, int absences, float avarage, EEnrollee status) {}
