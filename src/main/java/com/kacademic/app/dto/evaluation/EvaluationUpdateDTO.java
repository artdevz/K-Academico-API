package com.kacademic.app.dto.evaluation;

import java.util.Optional;

public record EvaluationUpdateDTO(
    Optional<Float> score
) {}
