package com.kacademico.app.dto.enrollee;

import java.util.UUID;

public record EnrolleeRequestDTO(
    UUID student,
    UUID grade
) {}
