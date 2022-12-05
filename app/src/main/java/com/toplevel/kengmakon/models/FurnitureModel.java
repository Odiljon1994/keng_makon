package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FurnitureModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private FurnitureData data;

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

    public FurnitureData getData() {
        return data;
    }

    public void setData(FurnitureData data) {
        this.data = data;
    }

    public FurnitureModel(int code, String message, FurnitureData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class FurnitureData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        private List<FurnitureDataItem> items;

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

        public List<FurnitureDataItem> getItems() {
            return items;
        }

        public void setItems(List<FurnitureDataItem> items) {
            this.items = items;
        }

        public FurnitureData(int currentPage, int totalPages, List<FurnitureDataItem> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class FurnitureDataItem {
        @SerializedName("id")
        private int id;
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
        @SerializedName("category")
        private FurnitureDataItemCategory category;
        @SerializedName("is_liked")
        private boolean is_liked;

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

        public FurnitureDataItemCategory getCategory() {
            return category;
        }

        public void setCategory(FurnitureDataItemCategory category) {
            this.category = category;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public FurnitureDataItem(int id, String name, String size, String description, String image_url, String image_url_preview, FurnitureDataItemCategory category, boolean is_liked) {
            this.id = id;
            this.name = name;
            this.size = size;
            this.description = description;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
            this.category = category;
            this.is_liked = is_liked;
        }
    }

    public class FurnitureDataItemCategory {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

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

        public FurnitureDataItemCategory(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
