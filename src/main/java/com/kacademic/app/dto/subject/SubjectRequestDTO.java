package com.kacademic.app.dto.subject;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record SubjectRequestDTO(

    UUID course,

    @Size(max=127)
    String name,

    @Size(max=255)
    String description,

    @Min(40)
    @Max(80)
    int duration,

    @Min(1)
    int semester,

    List<UUID> prerequisites

) {}
