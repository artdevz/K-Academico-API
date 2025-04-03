package com.kacademic.app.dto.professor;

import java.util.Optional;

public record ProfessorUpdateDTO(
    Optional<Integer> wage
) {}
