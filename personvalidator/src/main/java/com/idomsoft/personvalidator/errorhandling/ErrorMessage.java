package com.idomsoft.personvalidator.errorhandling;

public enum ErrorMessage {
    INVALID_NAT_CODE("Nationality code invalid"),
    INVALID_NAT_DECODE("Nationality decode invalid"),
    INVALID_GENDER("Gender invalid"),
    INVALID_BIRTH_DATE("Birth date invalid"),
    INVALID_MOTHERS_NAME("Mother's name invalid"),
    INVALID_NAME("Name invalid"),
    INVALID_BIRTH_NAME("Birth name invalid");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
