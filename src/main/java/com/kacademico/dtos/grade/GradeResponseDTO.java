package com.kacademico.dtos.grade;

import java.util.List;
import java.util.UUID;

import com.kacademico.utils.Timetable;

public record GradeResponseDTO(UUID id, String subject, String professor, int capacity, String semester, String locate, List<Timetable> timetable) {}
