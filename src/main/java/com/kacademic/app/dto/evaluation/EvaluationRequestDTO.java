package com.kacademic.app.dto.evaluation;

import java.util.UUID;

public record EvaluationRequestDTO(
    UUID enrollee,
    UUID exam,
    float score
) {}
