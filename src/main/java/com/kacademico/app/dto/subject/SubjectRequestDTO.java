package com.kacademico.app.dto.subject;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record SubjectRequestDTO(

    UUID course,

    @Size(max=128)
    String name,

    @Size(max=255)
    String description,

    @Min(40)
    @Max(80)
    int duration,

    @Min(1)
    int semester,

    boolean isRequired,

    List<UUID> prerequisites

) {}
