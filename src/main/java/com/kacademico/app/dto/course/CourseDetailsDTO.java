package com.kacademico.app.dto.course;

import java.util.List;

import com.kacademico.app.dto.subject.SubjectResponseDTO;

public record CourseDetailsDTO(
    CourseResponseDTO summary,
    List<SubjectResponseDTO> subjects
) {}
