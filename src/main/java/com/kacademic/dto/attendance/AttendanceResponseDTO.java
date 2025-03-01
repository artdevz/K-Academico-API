package com.kacademic.dto.attendance;

import java.util.UUID;

public record AttendanceResponseDTO(
    UUID id,
    UUID enrollee,
    UUID lesson,
    boolean isAbsent
) {}
