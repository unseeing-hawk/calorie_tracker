package ru.unfatcrew.restcalorietracker.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

public class FiniteFloatValidator implements ConstraintValidator<FiniteFloat, Float> {
    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value <= Float.MAX_VALUE;
    }
}
