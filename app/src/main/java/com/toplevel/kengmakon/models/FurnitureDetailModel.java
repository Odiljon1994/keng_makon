package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class FurnitureDetailModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private FurnitureModel.FurnitureDataItem data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FurnitureModel.FurnitureDataItem getData() {
        return data;
    }

    public void setData(FurnitureModel.FurnitureDataItem data) {
        this.data = data;
    }

    public FurnitureDetailModel(int code, String message, FurnitureModel.FurnitureDataItem data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
