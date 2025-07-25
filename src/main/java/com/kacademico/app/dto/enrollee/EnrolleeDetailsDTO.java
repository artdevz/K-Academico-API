package com.kacademico.app.dto.enrollee;

import java.util.List;

import com.kacademico.app.dto.attendance.AttendanceResponseDTO;
import com.kacademico.app.dto.evaluation.EvaluationResponseDTO;

public record EnrolleeDetailsDTO(
    EnrolleeResponseDTO summary,
    List<EvaluationResponseDTO> evaluations,
    List<AttendanceResponseDTO> attendances
) {}