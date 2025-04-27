package com.kacademico.app.dto.lesson;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Size;

public record LessonRequestDTO(

    UUID grade,

    @Size(max=32)
    String topic,

    LocalDate date

) {}
