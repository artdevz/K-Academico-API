package com.kacademico.app.dto.attendance;

import java.util.UUID;

public record AttendanceRequestDTO(
    UUID enrollee,
    UUID lesson,
    boolean isAbsent
) {}
