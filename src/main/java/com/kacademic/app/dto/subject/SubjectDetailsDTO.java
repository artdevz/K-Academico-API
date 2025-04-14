package com.kacademic.app.dto.subject;

import java.util.List;

import com.kacademic.app.dto.equivalence.EquivalenceResponseDTO;

public record SubjectDetailsDTO(
    SubjectResponseDTO summary,
    List<EquivalenceResponseDTO> prerequisites
) {}
