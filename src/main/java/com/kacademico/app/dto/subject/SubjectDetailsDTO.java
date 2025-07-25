package com.kacademico.app.dto.subject;

import java.util.List;

import com.kacademico.app.dto.equivalence.EquivalenceResponseDTO;

public record SubjectDetailsDTO(
    SubjectResponseDTO summary,
    List<EquivalenceResponseDTO> prerequisites
) {}
