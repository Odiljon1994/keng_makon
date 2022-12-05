package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class UserInfoModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserInfoData data;

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

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }

    public UserInfoModel(int code, String message, UserInfoData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class UserInfoData {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("email")
        private String email;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public UserInfoData(int id, String name, String phone, String email) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }
}
