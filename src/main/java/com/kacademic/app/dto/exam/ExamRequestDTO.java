package com.kacademic.app.dto.exam;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Max;

public record ExamRequestDTO(
    
    UUID grade,
    
    String name,

    @Max(10)
    int maximum,

    LocalDate date
    
) {}
