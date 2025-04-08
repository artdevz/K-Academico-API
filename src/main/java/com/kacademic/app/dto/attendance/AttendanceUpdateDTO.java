package com.kacademic.app.dto.attendance;

import java.util.Optional;

public record AttendanceUpdateDTO(
    Optional<Boolean> isAbsent
) {}
