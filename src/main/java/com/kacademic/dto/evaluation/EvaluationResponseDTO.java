package com.kacademic.dto.evaluation;

import java.util.UUID;

public record EvaluationResponseDTO(UUID id, UUID enrollee, UUID grade, UUID exam, float score) {}
