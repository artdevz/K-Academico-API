package com.kacademico.app.dto.course;

import java.util.Optional;

public record CourseUpdateDTO(
    Optional<String> name,
    Optional<String> description
) {}
