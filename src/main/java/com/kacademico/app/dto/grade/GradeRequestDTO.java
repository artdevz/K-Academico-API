package com.kacademico.app.dto.grade;

import java.util.List;
import java.util.UUID;

import com.kacademico.domain.models.values.Timetable;
import com.kacademico.shared.utils.Semester;

import jakarta.validation.constraints.Min;

public record GradeRequestDTO(
    
    UUID subject,
    
    UUID professor,

    @Min(20)
    int capacity,

    @Semester String semester,
    
    String locate,
    
    List<Timetable> timetable
    
) {}
