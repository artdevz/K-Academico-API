package com.kacademico.app.dto.course;

import jakarta.validation.constraints.Min;

public record WorkloadDTO(
    @Min(0)
    int electiveHours,
    @Min(0)
    int complementaryHours,
    @Min(0)
    int internshipHours
) {}
