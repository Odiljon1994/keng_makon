package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionsModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ActionsData data;

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

    public ActionsData getData() {
        return data;
    }

    public void setData(ActionsData data) {
        this.data = data;
    }

    public ActionsModel(int code, String message, ActionsData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class ActionsData {
        @SerializedName("currentPage")
        private int currentPage;
        @SerializedName("totalPages")
        private int totalPages;
        @SerializedName("items")
        private List<ActionsItems> items;

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

        public List<ActionsItems> getItems() {
            return items;
        }

        public void setItems(List<ActionsItems> items) {
            this.items = items;
        }

        public ActionsData(int currentPage, int totalPages, List<ActionsItems> items) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.items = items;
        }
    }


    public class ActionsItems {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("enabled")
        private int enabled;
        @SerializedName("start_date")
        private String start_date;
        @SerializedName("end_date")
        private String end_date;
        @SerializedName("category")
        private String category;
        @SerializedName("file_name")
        private String file_name;

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

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public ActionsItems(int id, String title, String description, String created_at, String updated_at, int enabled, String start_date, String end_date, String category, String file_name) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.enabled = enabled;
            this.start_date = start_date;
            this.end_date = end_date;
            this.category = category;
            this.file_name = file_name;
        }
    }
}
