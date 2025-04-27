package com.kacademico.app.dto.student;

import java.util.UUID;

import com.kacademico.app.dto.user.UserRequestDTO;

public record StudentRequestDTO(
    UserRequestDTO user,
    UUID course
) {}
