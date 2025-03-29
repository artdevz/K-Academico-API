package com.kacademic.app.dto.enrollee;

import java.util.List;

import com.kacademic.app.dto.attendance.AttendanceResponseDTO;
import com.kacademic.app.dto.evaluation.EvaluationResponseDTO;

public record EnrolleeDetailsDTO(
    EnrolleeResponseDTO summary,
    List<EvaluationResponseDTO> evaluations,
    List<AttendanceResponseDTO> attendances
) {}