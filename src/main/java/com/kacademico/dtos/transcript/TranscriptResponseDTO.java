package com.kacademico.dtos.transcript;

import java.util.Set;
import java.util.UUID;

import com.kacademico.models.Enrollee;

public record TranscriptResponseDTO(UUID id, String student, Set<Enrollee> enrollees) {}
