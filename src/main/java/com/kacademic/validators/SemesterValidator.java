package com.kacademic.validators;

import com.kacademic.utils.Semester;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SemesterValidator implements ConstraintValidator<Semester, String> {
    
    private static final String SEMESTER_REGEX = "^[0-9]{2}\\.(1|2)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
        if (value == null) return false;
        return value.matches(SEMESTER_REGEX);

    }

}
