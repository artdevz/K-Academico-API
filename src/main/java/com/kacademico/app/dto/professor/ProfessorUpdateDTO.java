package com.kacademico.app.dto.professor;

import java.util.Optional;

public record ProfessorUpdateDTO(
    Optional<Integer> wage
) {}
