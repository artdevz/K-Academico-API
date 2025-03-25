package com.kacademic.dto.course;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CourseRequestDTO(
    
    @Size(min=4, max=160, message="Course name must be between 4 and 160 characters")
    String name,
    
    @Pattern(regexp = "^\\d+$", message = "Course code must contain only numbers")
    @Size(min=2, max=2, message="Course code must contain exactly 2 characters")
    String code,
    
    @Size(max = 256, message = "Description must not exceed 256 characters")
    String description
) {}
