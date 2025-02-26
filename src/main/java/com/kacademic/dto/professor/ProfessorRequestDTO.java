package com.kacademic.dto.professor;

import com.kacademic.dto.user.UserRequestDTO;

public record ProfessorRequestDTO(UserRequestDTO user, int wage) {}
