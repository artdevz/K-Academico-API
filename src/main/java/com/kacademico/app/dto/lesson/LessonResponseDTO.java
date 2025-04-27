package com.kacademico.app.dto.lesson;

import java.time.LocalDate;
import java.util.UUID;

import com.kacademico.domain.enums.ELesson;

public record LessonResponseDTO(
    UUID id,
    UUID grade,
    String topic,
    LocalDate date,
    ELesson status
) {}
