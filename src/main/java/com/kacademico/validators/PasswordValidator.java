package com.kacademico.validators;

import com.kacademico.utils.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
        if (value == null) return false;
        return value.matches(PASSWORD_REGEX);
        
    }
}
