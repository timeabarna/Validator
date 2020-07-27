package com.idomsoft.validatormain.model;

import java.io.Serializable;
import java.util.List;

public class IdValidatorRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<OkmanyDTO> idList;
    private List<String> validationErrors;

    public IdValidatorRequest(List<OkmanyDTO> idList, List<String> validationErrors) {
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
