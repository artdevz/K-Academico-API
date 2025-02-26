package com.kacademic.dto.student;

import java.util.UUID;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.enums.EShift;

public record StudentRequestDTO(UserRequestDTO user, UUID course, EShift shift) {}
