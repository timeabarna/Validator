package com.idomsoft.validatormain.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SzemelyDTO implements Serializable {
    private static final long serialVersionUID = 4L;

    @NotBlank private String visNev;
    @NotBlank private String szulNev;
    @NotBlank private String aNev;
    @NotNull private Date szulDat;
    @NotBlank private String neme;
    @NotBlank private String allampKod;
    private String allampDekod;
    @NotNull @Valid private List<OkmanyDTO> okmLista;

    public String getVisNev() {
        return visNev;
    }

    public void setVisNev(String visNev) {
        this.visNev = visNev;
    }

    public String getSzulNev() {
        return szulNev;
    }

    public void setSzulNev(String szulNev) {
        this.szulNev = szulNev;
    }

    public String getaNev() {
        return aNev;
    }

    public void setaNev(String aNev) {
        this.aNev = aNev;
    }

    public Date getSzulDat() {
        return szulDat;
    }

    public void setSzulDat(Date szulDat) {
        this.szulDat = szulDat;
    }

    public String getNeme() {
        return neme;
    }

    public void setNeme(String neme) {
        this.neme = neme;
    }

    public String getAllampKod() {
        return allampKod;
    }

    public void setAllampKod(String allampKod) {
        this.allampKod = allampKod;
    }

    public String getAllampDekod() {
        return allampDekod;
    }

    public void setAllampDekod(String allampDekod) {
        this.allampDekod = allampDekod;
    }

    public List<OkmanyDTO> getOkmLista() {
        return okmLista;
    }

    public void setOkmLista(List<OkmanyDTO> okmLista) {
        this.okmLista = okmLista;
    }
    
}
