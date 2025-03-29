package com.kacademic.app.dto.grade;

import java.util.Optional;

import com.kacademic.domain.enums.EGrade;

public record GradeUpdateDTO(Optional<EGrade> status) {}
