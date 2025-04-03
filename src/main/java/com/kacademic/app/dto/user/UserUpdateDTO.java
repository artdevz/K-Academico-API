package com.kacademic.app.dto.user;

import java.util.Optional;

import com.kacademic.shared.utils.Password;

// import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    // @Size(min=3, max=48, message="Nome deve conter entre 3 a 48 caractéres.") // Fazer um OptionalSizeValidator
    Optional<String> name,
    
    @Password 
    Optional<String> password) {}
