package com.kacademico.dtos.evaluation;

import java.util.UUID;

public record EvaluationResponseDTO(UUID id, String enrollee, UUID grade, String exam, float score) {}