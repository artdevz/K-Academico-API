package com.kacademic.dto.transcript;

import java.util.Set;
import java.util.UUID;

import com.kacademic.models.Enrollee;

public record TranscriptResponseDTO(UUID id, String student, Set<Enrollee> enrollees) {}
