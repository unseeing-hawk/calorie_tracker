package ru.unfatcrew.restcalorietracker.rest.exception_handling;

import java.util.List;

public class ValidationErrorResponse {

    private final int status;
    private final List<Violation> violationList;

    public ValidationErrorResponse(int status, List<Violation> violationList) {
        this.status = status;
        this.violationList = violationList;
    }

    public int getStatus() {
        return status;
    }

    public List<Violation> getViolationList() {
        return violationList;
    }
}
