package com.kacademico.app.dto.student;

import java.util.List;

import com.kacademico.app.dto.enrollee.EnrolleeResponseDTO;

public record StudentDetailsDTO(
    StudentResponseDTO summary,
    List<EnrolleeResponseDTO> enrollees
) {}

