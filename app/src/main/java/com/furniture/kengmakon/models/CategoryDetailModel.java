package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryDetailModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CategoryDetailData data;

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

    public CategoryDetailData getData() {
        return data;
    }

    public void setData(CategoryDetailData data) {
        this.data = data;
    }

    public CategoryDetailModel(int code, String message, CategoryDetailData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class CategoryDetailData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        private List<CategoryDetailDataItem> items;

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

        public List<CategoryDetailDataItem> getItems() {
            return items;
        }

        public void setItems(List<CategoryDetailDataItem> items) {
            this.items = items;
        }

        public CategoryDetailData(int currentPage, int totalPages, List<CategoryDetailDataItem> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class CategoryDetailDataItem {
        @SerializedName("furniture_id")
        private int furniture_id;
        @SerializedName("category_id")
        private int category_id;
        @SerializedName("name")
        private String name;
        @SerializedName("size")
        private String size;
        @SerializedName("description")
        private String description;
        @SerializedName("image_url")
        private String image_url;
        @SerializedName("image_url_preview")
        private String image_url_preview;
        @SerializedName("is_liked")
        private boolean is_liked;

        public int getFurniture_id() {
            return furniture_id;
        }

        public void setFurniture_id(int furniture_id) {
            this.furniture_id = furniture_id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public CategoryDetailDataItem(int furniture_id, int category_id, String name, String size, String description, String image_url, String image_url_preview, boolean is_liked) {
            this.furniture_id = furniture_id;
            this.category_id = category_id;
            this.name = name;
            this.size = size;
            this.description = description;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
            this.is_liked = is_liked;
        }
    }
}
