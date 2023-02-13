package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordReqModel {

    @SerializedName("email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ForgotPasswordReqModel(String email) {
        this.email = email;
    }
}
