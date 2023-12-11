package ru.unfatcrew.clientcalorietracker.controller.exception_handler.server_response;

import java.util.List;

public class ValidationErrorResponse {
    private int status;
    private List<Violation> violationList;

    public ValidationErrorResponse() {}

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
