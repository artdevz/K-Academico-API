package com.kacademic.dto.student;

import java.util.List;

import com.kacademic.dto.enrollee.EnrolleeResponseDTO;

public record StudentDetailsDTO(StudentResponseDTO summary, List<EnrolleeResponseDTO> enrollees) {}
