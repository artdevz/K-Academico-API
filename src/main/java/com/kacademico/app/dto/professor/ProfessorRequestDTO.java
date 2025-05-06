package com.kacademico.app.dto.professor;

import com.kacademico.app.dto.user.UserRequestDTO;

public record ProfessorRequestDTO(
    UserRequestDTO user
) {}
