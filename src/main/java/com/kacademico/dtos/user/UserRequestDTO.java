package com.kacademico.dtos.user;

import com.kacademico.utils.Password;

public record UserRequestDTO(String name, String email, @Password String password) {}
