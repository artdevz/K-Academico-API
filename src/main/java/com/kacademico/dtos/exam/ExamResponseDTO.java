package com.kacademico.dtos.exam;

import java.time.LocalDate;
import java.util.UUID;

public record ExamResponseDTO(UUID id, String grade, String name, float score, int maximum, LocalDate date) {}
