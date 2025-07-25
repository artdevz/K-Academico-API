package com.kacademico.app.dto.grade;

import java.util.List;
import java.util.UUID;

import com.kacademico.domain.models.values.Schedule;
import com.kacademico.domain.models.values.Timetable;

import jakarta.validation.constraints.Min;

public record GradeRequestDTO(
    
    UUID subject,
    
    UUID professor,

    @Min(value = 20, message = "The minimum allowed capacity is 20")
    int capacity,

    Schedule schedule,

    List<Timetable> timetable
    
) {}
