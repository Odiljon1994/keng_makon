package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class PushTokenReqModel {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PushTokenReqModel(String token) {
        this.token = token;
    }
}
