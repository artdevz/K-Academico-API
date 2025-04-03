package com.kacademic.app.dto.course;

public record CourseRequestDTO(
    String name,
    String code,
    int duration,
    String description
) {}
