package com.kacademic.app.dto.student;

import java.util.List;

import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;

public record StudentDetailsDTO(StudentResponseDTO summary, List<EnrolleeResponseDTO> enrollees) {}
