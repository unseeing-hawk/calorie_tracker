package ru.unfatcrew.restcalorietracker.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.unfatcrew.restcalorietracker.validation.validator.FiniteFloatValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FiniteFloatValidator.class)
public @interface FiniteFloat {
    String message() default "must be a finite float";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
