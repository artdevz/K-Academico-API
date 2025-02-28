package com.kacademic.dto.enrollee;

import java.util.List;

import com.kacademic.dto.evaluation.EvaluationResponseDTO;

public record EnrolleeDetailsDTO(EnrolleeResponseDTO summary, List<EvaluationResponseDTO> evaluations) {}
