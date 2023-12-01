package ru.unfatcrew.restcalorietracker.rest.exception_handling.exception;

import org.springframework.http.HttpStatus;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.util.List;

public class IllegalRequestArgumentException extends IllegalArgumentException {

    private final int status = HttpStatus.BAD_REQUEST.value();

    private List<Violation> violationList;

    public IllegalRequestArgumentException() {
    }

    public IllegalRequestArgumentException(List<Violation> violationList) {
        this.violationList = violationList;
    }

    public IllegalRequestArgumentException(String s) {
        super(s);
    }

    public IllegalRequestArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRequestArgumentException(Throwable cause) {
        super(cause);
    }

    public int getStatus() {
        return status;
    }

    public List<Violation> getViolationList() {
        return violationList;
    }
}
