package com.kacademic.shared.validators;

import java.util.Optional;

import com.kacademic.shared.utils.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalPasswordValidator implements ConstraintValidator<Password, Optional<String>> {
    
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$";

    @Override
    public boolean isValid(Optional<String> value, ConstraintValidatorContext context) {
        
        if (!value.isPresent()) return true;
        return value.get().matches(PASSWORD_REGEX);
        
    }

}
