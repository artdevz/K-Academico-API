package com.kacademic.app.dto.equivalence;

import java.util.List;
import java.util.UUID;

public record EquivalenceDetailsDTO(
    EquivalenceResponseDTO summary,
    List<UUID> subjects
) {}
