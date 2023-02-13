package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class UpdateUsernameReqModel {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateUsernameReqModel(String name) {
        this.name = name;
    }
}
