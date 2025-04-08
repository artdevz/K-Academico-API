package com.kacademic.app.dto.course;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseRequestDTO(
    @Size(min=4, max=160, message="Course name must be between 4 and 160 characters")
    String name,

    @Pattern(regexp = "^\\d+$", message = "Course code must contain only numbers")
    @Size(min=3, max=3, message="Course code must contain exactly 3 characters")
    String code,

    @Size(max=256)
    String description
) {}
