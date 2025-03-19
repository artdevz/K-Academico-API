package com.kacademic.dto.user;

import java.util.Optional;

public record UserUpdateDTO(Optional<String> name, Optional<String> password) {}
