package com.kacademic.app.dto.course;

import java.util.List;

import com.kacademic.app.dto.subject.SubjectResponseDTO;

public record CourseDetailsDTO(CourseResponseDTO summary, List<SubjectResponseDTO> subjects) {}
