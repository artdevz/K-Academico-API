package com.kacademico.app.dto.grade;

import java.util.List;
import java.util.UUID;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.values.Timetable;

public record GradeResponseDTO(
    UUID id,
    UUID subject,
    UUID professor,
    int capacity,
    int currentStudents,
    String semester,
    EGrade status,
    String locate,
    List<Timetable> timetable
) {}
