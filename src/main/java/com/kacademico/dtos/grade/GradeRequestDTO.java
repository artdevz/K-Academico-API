package com.kacademico.dtos.grade;

import java.util.List;
import java.util.UUID;

import com.kacademico.utils.Timetable;

public record GradeRequestDTO(UUID subject, UUID professor, int capacity, List<Timetable> timetable, String locate) {}
