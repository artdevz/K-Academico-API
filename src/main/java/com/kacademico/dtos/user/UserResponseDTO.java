package com.kacademico.dtos.user;

import java.util.UUID;

public record UserResponseDTO(UUID id, String enrollment, String name, String email) {}
