package com.kacademic.dto.subject;

import java.util.Optional;

import com.kacademic.enums.ESubject;

public record SubjectUpdateDTO(
    Optional<ESubject> type,
    Optional<String> name,
    Optional<String> description,
    Optional<Integer> duration,
    Optional<Integer> semester
) {}
