package com.kacademic.dto.lesson;

import java.time.LocalDate;
import java.util.UUID;

public record LessonRequestDTO(
    UUID grade,
    String topic,
    LocalDate date
) {}
