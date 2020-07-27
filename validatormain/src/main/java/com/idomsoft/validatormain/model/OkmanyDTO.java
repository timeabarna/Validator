package com.idomsoft.validatormain.model;

import java.io.Serializable;
import java.util.Date;

public class OkmanyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String okmTipus;
    private String okmanySzam;
    private byte[] okmanyKep;
    private Date lejarDat;
    private boolean ervenyes;

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
