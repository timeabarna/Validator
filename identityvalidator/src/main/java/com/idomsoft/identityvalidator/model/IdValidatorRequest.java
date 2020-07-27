package com.idomsoft.identityvalidator.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class IdValidatorRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull @Valid private List<OkmanyDTO> idList;
    @NotNull private List<String> validationErrors;

    public IdValidatorRequest(@NotNull @Valid List<OkmanyDTO> idList, @NotNull List<String> validationErrors) {
        this.idList = idList;
        this.validationErrors = validationErrors;
    }

    public List<OkmanyDTO> getIdList() {
        return idList;
    }

    public void setIdList(List<OkmanyDTO> idList) {
        this.idList = idList;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
