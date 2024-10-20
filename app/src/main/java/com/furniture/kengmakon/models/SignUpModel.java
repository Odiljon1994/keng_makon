package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class SignUpModel {

    @SerializedName("name")
    private String name;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("email_address")
    private String email_address;
    @SerializedName("password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SignUpModel(String name, String phone_number, String email_address, String password) {
        this.name = name;
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.password = password;
    }



}
