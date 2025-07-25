package com.kacademico.shared.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kacademico.shared.validators.OptionalPasswordValidator;
import com.kacademico.shared.validators.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = { PasswordValidator.class, OptionalPasswordValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    
    String message() default "The password must contain at least one lowercase letter, one uppercase letter, one number, one special character, and be between 8 and 32 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}