package com.kacademic.dto.grade;

import java.util.Optional;

import com.kacademic.enums.EGrade;

public record GradeUpdateDTO(Optional<EGrade> status) {}
