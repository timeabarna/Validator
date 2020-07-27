package com.idomsoft.identityvalidator.error;

public enum ErrorMessage {
    INVALID_ID_CODE("Identification code invalid for "),
    INVALID_ID_NUMBER("Identification number invalid for "),
    INVALID_IMAGE_FORMAT("Image format invalid for "),
    INVALID_IMAGE_SIZE("Image size invalid for "),
    INVALID_MULTIPLE_ID_ACTIVE("More than one ids active from the same type");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
