package ru.unfatcrew.restcalorietracker.rest.exception_handling;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.ValidationErrorResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalRestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(
            ConstraintViolationException e
    ) {
        List<Violation> violationList = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), violationList);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        List<Violation> violationList = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), violationList);
    }

    @ResponseBody
    @ExceptionHandler(IllegalRequestArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleIllegalRequestArgumentException(
            IllegalRequestArgumentException e
    ) {
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getViolationList());
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleResourceNotFoundException(
            ResourceNotFoundException e
    ) {
        return new ValidationErrorResponse(HttpStatus.NOT_FOUND.value(), e.getViolationList());
    }
}
