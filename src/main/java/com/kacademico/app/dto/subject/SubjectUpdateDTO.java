package com.kacademico.app.dto.subject;

import java.util.Optional;

public record SubjectUpdateDTO(
    Optional<String> name,
    Optional<String> description,
    Optional<Integer> duration,
    Optional<Boolean> isRequired,
    Optional<Integer> semester
) {}
