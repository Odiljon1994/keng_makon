package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private SetData data;

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

    public SetData getData() {
        return data;
    }

    public void setData(SetData data) {
        this.data = data;
    }

    public SetModel(int code, String message, SetData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class SetData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        List<SetDataItem> items;

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

        public List<SetDataItem> getItems() {
            return items;
        }

        public void setItems(List<SetDataItem> items) {
            this.items = items;
        }

        public SetData(int currentPage, int totalPages, List<SetDataItem> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class SetDataItem {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("item_count")
        private int item_count;
        @SerializedName("image_url")
        private String image_url;
        @SerializedName("image_url_preview")
        private String image_url_preview;

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

        public int getItem_count() {
            return item_count;
        }

        public void setItem_count(int item_count) {
            this.item_count = item_count;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getImage_url_preview() {
            return image_url_preview;
        }

        public void setImage_url_preview(String image_url_preview) {
            this.image_url_preview = image_url_preview;
        }

        public SetDataItem(int id, String name, int item_count, String image_url, String image_url_preview) {
            this.id = id;
            this.name = name;
            this.item_count = item_count;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
        }
    }
}
