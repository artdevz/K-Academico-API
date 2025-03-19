package com.kacademic.dto.course;

import java.util.Optional;

public record CourseUpdateDTO(Optional<String> name, Optional<String> description) {}
