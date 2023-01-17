package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class NameModel {

    @SerializedName("uz")
    private String uz;
    @SerializedName("ru")
    private String ru;
    @SerializedName("en")
    private String en;

    public String getUz() {
        return uz;
    }

    public void setUz(String uz) {
        this.uz = uz;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public NameModel(String uz, String ru, String en) {
        this.uz = uz;
        this.ru = ru;
        this.en = en;
    }
}
