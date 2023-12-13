package ru.unfatcrew.clientcalorietracker.controller.exception_handler.server_response;

public class Violation {
    private String fieldName;
    private String message;

    public Violation() {}

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
