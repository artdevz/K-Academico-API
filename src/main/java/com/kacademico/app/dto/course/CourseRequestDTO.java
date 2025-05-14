package com.kacademico.app.dto.course;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseRequestDTO(
    @Size(min=8, max=128, message="Course name must be between 4 and 128 characters")
    String name,

    @Pattern(regexp = "^\\d+$", message = "Course code must contain only numbers")
    @Size(min=3, max=3, message="Course code must contain exactly 3 digits")
    String code,

    @Size(max=255, message = "Description must be at most 255 characters")
    String description
) {}
