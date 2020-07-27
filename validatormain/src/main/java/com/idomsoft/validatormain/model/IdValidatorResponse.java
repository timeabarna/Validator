package com.idomsoft.validatormain.model;

import java.io.Serializable;
import java.util.List;

public class IdValidatorResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<OkmanyDTO> idList;
    private List<String> validationErrors;

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
