package com.kacademic.dto.course;

import java.util.Optional;

import jakarta.validation.constraints.Size;

public record CourseUpdateDTO(

    @Size(min=4, max=160, message="Course name must be between 4 and 160 characters")
    Optional<String> name,

    @Size(max = 256, message = "Description must not exceed 256 characters")
    Optional<String> description
) {}
