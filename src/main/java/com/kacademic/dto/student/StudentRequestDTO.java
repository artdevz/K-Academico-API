package com.kacademic.dto.student;

import java.util.UUID;

import com.kacademic.dto.user.UserRequestDTO;

public record StudentRequestDTO(UserRequestDTO user, UUID course) {}
