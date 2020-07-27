package com.idomsoft.personvalidator.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class PersonValidatorRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull @Valid private SzemelyDTO person;
    @NotNull private List<String> validationErrors;

    public PersonValidatorRequest(@NotNull @Valid SzemelyDTO person, @NotNull List<String> validationErrors) {
        this.person = person;
        this.validationErrors = validationErrors;
    }

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
