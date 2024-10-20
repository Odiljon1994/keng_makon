package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CashbackModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CashbackData data;

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

    public CashbackData getData() {
        return data;
    }

    public void setData(CashbackData data) {
        this.data = data;
    }

    public CashbackModel(int code, String message, CashbackData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class CashbackData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("data")
        private CashbackInnerData data;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public CashbackInnerData getData() {
            return data;
        }

        public void setData(CashbackInnerData data) {
            this.data = data;
        }

        public CashbackData(int currentPage, int totalPages, CashbackInnerData data) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.data = data;
        }
    }

    public class CashbackInnerData {
        @SerializedName("items")
        private List<CashbackDataItems> items;
        @SerializedName("cashback")
        private CashbackDataTotal cashback;

        public List<CashbackDataItems> getItems() {
            return items;
        }

        public void setItems(List<CashbackDataItems> items) {
            this.items = items;
        }

        public CashbackDataTotal getCashback() {
            return cashback;
        }

        public void setCashback(CashbackDataTotal cashback) {
            this.cashback = cashback;
        }

        public CashbackInnerData(List<CashbackDataItems> items, CashbackDataTotal cashback) {
            this.items = items;
            this.cashback = cashback;
        }
    }

    public class CashbackDataTotal {
        @SerializedName("total")
        private double total;
        @SerializedName("remain")
        private double remain;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getRemain() {
            return remain;
        }

        public void setRemain(double remain) {
            this.remain = remain;
        }

        public CashbackDataTotal(double total, double remain) {
            this.total = total;
            this.remain = remain;
        }

        //        public int getTotal() {
//            return total;
//        }
//
//        public void setTotal(int total) {
//            this.total = total;
//        }
//
//        public int getRemain() {
//            return remain;
//        }
//
//        public void setRemain(int remain) {
//            this.remain = remain;
//        }
//
//        public CashbackDataTotal(int total, int remain) {
//            this.total = total;
//            this.remain = remain;
//        }
    }

    public class CashbackDataItems {
        @SerializedName("amount")
        private int amount;
        @SerializedName("date")
        private String date;
        @SerializedName("percent")
        private float percent;
        @SerializedName("note")
        private String note;
        @SerializedName("order_id")
        private int order_id;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public CashbackDataItems(int amount, String date, float percent, String note, int order_id) {
            this.amount = amount;
            this.date = date;
            this.percent = percent;
            this.note = note;
            this.order_id = order_id;
        }
    }
}
