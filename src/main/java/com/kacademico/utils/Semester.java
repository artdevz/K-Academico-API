package com.kacademico.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kacademico.validators.SemesterValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = SemesterValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Semester {
    
    String message() default "O Código do Semestre deve está na formatação 'Ano.Semestre', sendo o Ano com 2 dígitos. Ex: '24.2'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
