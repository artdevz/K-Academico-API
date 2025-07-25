package com.kacademico.app.dto.evaluation;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record EvaluationRequestDTO(
    UUID enrollee,
    UUID exam,

    @DecimalMax(value = "10.0", message = "Score must be at most {value}")
    @DecimalMin(value = "0.0", message = "Score must be at least {value}")
    Float score
) {}
