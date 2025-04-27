package com.kacademico.app.dto.attendance;

import java.util.Optional;

public record AttendanceUpdateDTO(
    Optional<Boolean> isAbsent
) {}
