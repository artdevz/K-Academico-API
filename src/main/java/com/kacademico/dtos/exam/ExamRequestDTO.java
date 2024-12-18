package com.kacademico.dtos.exam;

import java.time.LocalDate;
import java.util.UUID;

public record ExamRequestDTO(UUID grade, String name, int maximum, LocalDate date) {}
