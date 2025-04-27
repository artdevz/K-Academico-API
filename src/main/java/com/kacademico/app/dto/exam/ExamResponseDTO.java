package com.kacademico.app.dto.exam;

import java.time.LocalDate;
import java.util.UUID;

public record ExamResponseDTO(
    UUID id,
    UUID grade,
    String name,
    int maximum,
    LocalDate date
) {}
