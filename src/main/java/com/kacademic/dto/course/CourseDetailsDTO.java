package com.kacademic.dto.course;

import java.util.List;

import com.kacademic.dto.subject.SubjectResponseDTO;

public record CourseDetailsDTO(CourseResponseDTO summary, List<SubjectResponseDTO> subjects) {}
