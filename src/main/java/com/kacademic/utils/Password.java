package com.kacademic.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kacademic.validators.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    
    String message() default "A senha deve conter pelo menos uma letra minúscula, uma letra maiúscula, um número, um caractere especial e ter entre 8 e 32 caracteres.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
