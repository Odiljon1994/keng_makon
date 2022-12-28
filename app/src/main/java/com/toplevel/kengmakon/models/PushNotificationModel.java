package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class PushNotificationModel {
    @SerializedName("en")
    private String en;
    @SerializedName("ru")
    private String ru;
    @SerializedName("uz")
    private String uz;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getUz() {
        return uz;
    }

    public void setUz(String uz) {
        this.uz = uz;
    }

    public PushNotificationModel(String en, String ru, String uz) {
        this.en = en;
        this.ru = ru;
        this.uz = uz;
    }
}
