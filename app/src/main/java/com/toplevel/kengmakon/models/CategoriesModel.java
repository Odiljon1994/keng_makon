package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CategoriesData data;

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

    public CategoriesData getData() {
        return data;
    }

    public void setData(CategoriesData data) {
        this.data = data;
    }

    public CategoriesModel(int code, String message, CategoriesData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class CategoriesData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        private List<CategoriesDataItem> items;

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

        public List<CategoriesDataItem> getItems() {
            return items;
        }

        public void setItems(List<CategoriesDataItem> items) {
            this.items = items;
        }

        public CategoriesData(int currentPage, int totalPages, List<CategoriesDataItem> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class CategoriesDataItem {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("image_url")
        private String image_url;

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

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public CategoriesDataItem(int id, String name, String image_url) {
            this.id = id;
            this.name = name;
            this.image_url = image_url;
        }
    }

    public class CategoriesItemName {
        @SerializedName("uz")
        private String uz;
        @SerializedName("ru")
        private String ru;
        @SerializedName("en")
        private String en;

        public String getUz() {
            return uz;
        }

        public void setUz(String uz) {
            this.uz = uz;
        }

        public String getRu() {
            return ru;
        }

        public void setRu(String ru) {
            this.ru = ru;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public CategoriesItemName(String uz, String ru, String en) {
            this.uz = uz;
            this.ru = ru;
            this.en = en;
        }
    }
}
