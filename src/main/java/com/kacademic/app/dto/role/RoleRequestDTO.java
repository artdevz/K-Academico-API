package com.kacademic.app.dto.role;

import jakarta.validation.constraints.Size;

public record RoleRequestDTO(

    @Size(min = 4, max = 20, message = "Role name must be between 4 and 20 characters")
    String name,

    String description

) {}
