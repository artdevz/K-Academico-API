package com.kacademico.app.dto.equivalence;

import java.util.List;
import java.util.UUID;

public record EquivalenceRequestDTO(
    String name,
    List<UUID> subjects
) {}
