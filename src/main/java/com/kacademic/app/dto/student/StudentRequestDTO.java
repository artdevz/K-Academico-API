package com.kacademic.app.dto.student;

import java.util.UUID;

import com.kacademic.app.dto.user.UserRequestDTO;

public record StudentRequestDTO(
    UserRequestDTO user,
    UUID course
) {}
