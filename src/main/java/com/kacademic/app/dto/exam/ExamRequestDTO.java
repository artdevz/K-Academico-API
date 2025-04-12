package com.kacademic.app.dto.exam;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

public record ExamRequestDTO(
    
    UUID grade,
    
    @Size(min = 3, max=16, message = "Exam name must be between 3 and 16 characters")
    String name,

    @Max(10)
    int maximum,

    LocalDate date
    
) {}
