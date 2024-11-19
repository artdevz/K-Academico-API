package com.kacademico.dtos.user;

import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email) {}
