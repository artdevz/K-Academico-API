package com.kacademico.app.dto.evaluation;

import java.util.Optional;

public record EvaluationUpdateDTO(
    // Fazer um Optional personalizado
    Optional<Float> score
) {}
