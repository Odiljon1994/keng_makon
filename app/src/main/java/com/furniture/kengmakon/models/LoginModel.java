package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public class LoginResModel {
        @SerializedName("code")
        private int code;
        @SerializedName("message")
        private String message;
        @SerializedName("data")
        private LoginData data;

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

        public LoginData getData() {
            return data;
        }

        public void setData(LoginData data) {
            this.data = data;
        }

        public LoginResModel(int code, String message, LoginData data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    public class LoginData {
        @SerializedName("user")
        private LoginDataUser user;
        @SerializedName("token")
        private String token;

        public LoginDataUser getUser() {
            return user;
        }

        public void setUser(LoginDataUser user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public LoginData(LoginDataUser user, String token) {
            this.user = user;
            this.token = token;
        }
    }
    public class LoginDataUser {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LoginDataUser(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}
