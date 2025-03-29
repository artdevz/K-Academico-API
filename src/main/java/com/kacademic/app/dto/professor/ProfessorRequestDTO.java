package com.kacademic.app.dto.professor;

import com.kacademic.app.dto.user.UserRequestDTO;

public record ProfessorRequestDTO(UserRequestDTO user, int wage) {}
