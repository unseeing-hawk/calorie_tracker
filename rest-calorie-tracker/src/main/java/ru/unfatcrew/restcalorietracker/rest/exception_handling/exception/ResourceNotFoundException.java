package ru.unfatcrew.restcalorietracker.rest.exception_handling.exception;

import org.springframework.http.HttpStatus;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.util.List;

public class ResourceNotFoundException extends RuntimeException {

    private final int status = HttpStatus.NOT_FOUND.value();

    private List<Violation> violationList;

    public ResourceNotFoundException() {

    }

    public ResourceNotFoundException(List<Violation> violationList) {
        this.violationList = violationList;
    }

    public ResourceNotFoundException(String message, List<Violation> violationList) {
        super(message);
        this.violationList = violationList;
    }

    public ResourceNotFoundException(String message, Throwable cause, List<Violation> violationList) {
        super(message, cause);
        this.violationList = violationList;
    }

    public ResourceNotFoundException(Throwable cause, List<Violation> violationList) {
        super(cause);
        this.violationList = violationList;
    }

    public int getStatus() {
        return status;
    }

    public List<Violation> getViolationList() {
        return violationList;
    }
}
