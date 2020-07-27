package com.idomsoft.identityvalidator.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class OkmanyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank private String okmTipus;
    @NotBlank private String okmanySzam;
    @NotNull private byte[] okmanyKep;
    @NotNull private Date lejarDat;
    private boolean ervenyes;

    public OkmanyDTO(@NotBlank String okmTipus, @NotBlank String okmanySzam, @NotNull byte[] okmanyKep, @NotNull Date lejarDat) {
        this.okmTipus = okmTipus;
        this.okmanySzam = okmanySzam;
        this.okmanyKep = okmanyKep;
        this.lejarDat = lejarDat;
    }

    public String getOkmTipus() {
        return okmTipus;
    }

    public void setOkmTipus(String okmTipus) {
        this.okmTipus = okmTipus;
    }

    public String getOkmanySzam() {
        return okmanySzam;
    }

    public void setOkmanySzam(String okmanySzam) {
        this.okmanySzam = okmanySzam;
    }

    public byte[] getOkmanyKep() {
        return okmanyKep;
    }

    public void setOkmanyKep(byte[] okmanyKep) {
        this.okmanyKep = okmanyKep;
    }

    public Date getLejarDat() {
        return lejarDat;
    }

    public void setLejarDat(Date lejarDat) {
        this.lejarDat = lejarDat;
    }

    public boolean isErvenyes() {
        return ervenyes;
    }

    public void setErvenyes(boolean ervenyes) {
        this.ervenyes = ervenyes;
    }
}
