package com.kacademic.dto.grade;

import java.util.List;
import java.util.UUID;

import com.kacademic.enums.EGrade;
import com.kacademic.utils.Timetable;

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
