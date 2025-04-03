package com.kacademic.app.dto.grade;

import java.util.List;
import java.util.UUID;

import com.kacademic.shared.utils.Semester;
import com.kacademic.shared.utils.Timetable;

public record GradeRequestDTO(
    UUID subject,
    UUID professor,
    int capacity,
    @Semester String semester,
    String locate,
    List<Timetable> timetable
) {}
