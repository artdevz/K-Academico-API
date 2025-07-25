package com.kacademico.app.dto.grade;

import java.util.Optional;

import com.kacademico.domain.enums.EGrade;

public record GradeUpdateDTO(
    Optional<EGrade> status
) {}
