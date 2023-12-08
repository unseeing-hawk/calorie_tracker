package ru.unfatcrew.restcalorietracker.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.unfatcrew.restcalorietracker.validation.annotation.LessThan10YearOldDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LessThan10YearOldDateValidator implements ConstraintValidator<LessThan10YearOldDate, String> {

    public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext constraintValidatorContext) {
        if (dateString == null) {
            return false;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateString, DateFormat);
        } catch (DateTimeParseException e) {
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        return date.isAfter(currentDate.minusYears(10))
                && date.isBefore(currentDate.plusDays(1));
    }
}
