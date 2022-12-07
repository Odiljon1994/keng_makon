package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class LikeModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private LikeData data;

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

    public LikeData getData() {
        return data;
    }

    public void setData(LikeData data) {
        this.data = data;
    }

    public LikeModel(int code, String message, LikeData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class LikeData {
        @SerializedName("furniture_id")
        private int furniture_id;
        @SerializedName("isLiked")
        private boolean isLiked;

        public int getFurniture_id() {
            return furniture_id;
        }

        public void setFurniture_id(int furniture_id) {
            this.furniture_id = furniture_id;
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public LikeData(int furniture_id, boolean isLiked) {
            this.furniture_id = furniture_id;
            this.isLiked = isLiked;
        }
    }

    public static class LikeReqModel {
        @SerializedName("furniture_id")
        private int furniture_id;

        public int getFurniture_id() {
            return furniture_id;
        }

        public void setFurniture_id(int furniture_id) {
            this.furniture_id = furniture_id;
        }

        public LikeReqModel(int furniture_id) {
            this.furniture_id = furniture_id;
        }
    }
}
