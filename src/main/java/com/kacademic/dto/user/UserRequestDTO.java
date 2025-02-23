package com.kacademic.dto.user;

import com.kacademic.utils.Password;

public record UserRequestDTO(String name, String email, @Password String password) {}
