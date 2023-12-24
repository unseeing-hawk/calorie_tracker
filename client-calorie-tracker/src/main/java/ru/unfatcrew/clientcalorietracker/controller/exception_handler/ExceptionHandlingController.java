package ru.unfatcrew.clientcalorietracker.controller.exception_handler;

import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import ru.unfatcrew.clientcalorietracker.controller.exception_handler.server_response.ValidationErrorResponse;
import ru.unfatcrew.clientcalorietracker.controller.exception_handler.server_response.Violation;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(ResourceAccessException.class)
    public String handleResourceAccessException(HttpServletRequest request, RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", "Failed to establish a connection with the server");
        return "redirect:" + getPathToGetEndpoint(request.getRequestURI()) + "?server-error";
    }

    @ExceptionHandler(RestClientResponseException.class)
    public String handleRestClientResponseException(HttpServletRequest request, RestClientResponseException exception,
                                                    RedirectAttributes attributes) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        
        try {
            ValidationErrorResponse response = exception.getResponseBodyAs(ValidationErrorResponse.class);
            if (response != null) {
                errorMessageBuilder.append("Error: ").append(response.getStatus()).append('\n');
                if (response.getViolationList() != null) {
                    for (Violation violation : response.getViolationList()) {
                        errorMessageBuilder.append(violation.getFieldName()).append(": ").append(violation.getMessage())
                                .append('\n');
                    }
                }
            }
        } catch (HttpMessageConversionException ex) {
            errorMessageBuilder.append("Internal Server Error: response conversion error in handleRestClientResponseException");
        }

        attributes.addFlashAttribute("errorMessage", errorMessageBuilder.toString());
        return "redirect:" + getPathToGetEndpoint(request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception,
                                                        RedirectAttributes attributes) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (FieldError error : exception.getFieldErrors()) {
            errorMessageBuilder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append('\n');
        }

        attributes.addFlashAttribute("errorMessage", errorMessageBuilder.toString());
        return "redirect:" + getPathToGetEndpoint(request.getRequestURI());
    }

    private static String getPathToGetEndpoint(String uri) {
        return "/" + uri.split("/")[1];
    }
}
