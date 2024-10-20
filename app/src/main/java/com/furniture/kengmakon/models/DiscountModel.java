package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class DiscountModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DiscountData data;

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

    public DiscountData getData() {
        return data;
    }

    public void setData(DiscountData data) {
        this.data = data;
    }

    public class DiscountData {

        @SerializedName("amount")
        private double amount;
        @SerializedName("percent")
        private int percent;
        @SerializedName("prev_amount")
        private double prev_amount;
        @SerializedName("prev_percent")
        private int prev_percent;
        @SerializedName("next_amount")
        private double next_amount;
        @SerializedName("next_percent")
        private int next_percent;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        public double getPrev_amount() {
            return prev_amount;
        }

        public void setPrev_amount(double prev_amount) {
            this.prev_amount = prev_amount;
        }

        public int getPrev_percent() {
            return prev_percent;
        }

        public void setPrev_percent(int prev_percent) {
            this.prev_percent = prev_percent;
        }

        public double getNext_amount() {
            return next_amount;
        }

        public void setNext_amount(double next_amount) {
            this.next_amount = next_amount;
        }

        public int getNext_percent() {
            return next_percent;
        }

        public void setNext_percent(int next_percent) {
            this.next_percent = next_percent;
        }
    }
}
