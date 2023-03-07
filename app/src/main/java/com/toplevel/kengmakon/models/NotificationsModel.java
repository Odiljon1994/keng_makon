package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private NotificationsData data;

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

    public NotificationsData getData() {
        return data;
    }

    public void setData(NotificationsData data) {
        this.data = data;
    }

    public NotificationsModel(int code, String message, NotificationsData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class NotificationsData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        private List<NotificationsDataItems> items;

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

        public List<NotificationsDataItems> getItems() {
            return items;
        }

        public void setItems(List<NotificationsDataItems> items) {
            this.items = items;
        }

        public NotificationsData(int currentPage, int totalPages, List<NotificationsDataItems> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }

    public class NotificationsDataItems {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("type")
        private String type;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("is_seen")
        private int is_seen;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getIs_seen() {
            return is_seen;
        }

        public void setIs_seen(int is_seen) {
            this.is_seen = is_seen;
        }

        public NotificationsDataItems(int id, String title, String description, String type, String created_at, String updated_at, int is_seen) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.type = type;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.is_seen = is_seen;
        }
    }
}
