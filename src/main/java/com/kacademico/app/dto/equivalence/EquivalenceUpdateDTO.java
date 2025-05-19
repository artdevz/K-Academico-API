package com.kacademico.app.dto.equivalence;

import java.util.List;
import java.util.UUID;

public record EquivalenceUpdateDTO(
    List<UUID> subjects
) {}
