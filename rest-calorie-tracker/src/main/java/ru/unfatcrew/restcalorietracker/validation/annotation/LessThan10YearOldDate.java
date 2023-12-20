package ru.unfatcrew.restcalorietracker.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.unfatcrew.restcalorietracker.validation.validator.LessThan10YearOldDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = LessThan10YearOldDateValidator.class)
public @interface LessThan10YearOldDate {

    String message() default "must match \"dd.MM.yyyy\" and be less than 10 years old";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
