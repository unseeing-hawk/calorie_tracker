package ru.unfatcrew.clientcalorietracker.controller.exception_handler;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(ResourceAccessException.class)
    public String handleResourceAccessException(HttpServletRequest request, RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", "Failed to establish a connection with the server");
        return "redirect:" + request.getRequestURI();
    }

    @ExceptionHandler(RestClientResponseException.class)
    public String handleRestClientResponceException(HttpServletRequest request, Exception exception,
                                                    RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", exception.getMessage());
        return "redirect:" + request.getRequestURI();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception,
                                                        RedirectAttributes attributes) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (FieldError error : exception.getFieldErrors()) {
            errorMessageBuilder.append(error.getField() + ": " + error.getDefaultMessage() + "\t");
        }

        attributes.addFlashAttribute("errorMessage", errorMessageBuilder.toString());
        return "redirect:" + request.getRequestURI();
    }
}
