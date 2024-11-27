package com.kacademico.dtos.enrollee;

import java.util.UUID;

import com.kacademico.enums.EEnrollee;

public record EnrolleeResponseDTO(UUID id, String student, String grade, int absences, float avarage, EEnrollee status) {}
