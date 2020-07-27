package com.idomsoft.validatormain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonValidatorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private SzemelyDTO person;
    private List<String> validationErrors = new ArrayList<>();

    public SzemelyDTO getPerson() {
        return person;
    }

    public void setPerson(SzemelyDTO person) {
        this.person = person;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
