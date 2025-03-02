package com.kacademic.dto.enrollee;

import java.util.List;

import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;

public record EnrolleeDetailsDTO(
    EnrolleeResponseDTO summary,
    List<EvaluationResponseDTO> evaluations,
    List<AttendanceResponseDTO> attendances
) {}