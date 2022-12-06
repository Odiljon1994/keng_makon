package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SetDetailModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    List<SetDetailData> data;

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

    public List<SetDetailData> getData() {
        return data;
    }

    public void setData(List<SetDetailData> data) {
        this.data = data;
    }

    public SetDetailModel(int code, String message, List<SetDetailData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class SetDetailData {
        @SerializedName("set")
        private SetDetailDataSet set;
        @SerializedName("furniture")
        private SetDetailDataFurniture furniture;
        @SerializedName("category")
        private FurnitureModel.FurnitureDataItemCategory category;

        public SetDetailDataSet getSet() {
            return set;
        }

        public void setSet(SetDetailDataSet set) {
            this.set = set;
        }

        public SetDetailDataFurniture getFurniture() {
            return furniture;
        }

        public void setFurniture(SetDetailDataFurniture furniture) {
            this.furniture = furniture;
        }

        public FurnitureModel.FurnitureDataItemCategory getCategory() {
            return category;
        }

        public void setCategory(FurnitureModel.FurnitureDataItemCategory category) {
            this.category = category;
        }

        public SetDetailData(SetDetailDataSet set, SetDetailDataFurniture furniture, FurnitureModel.FurnitureDataItemCategory category) {
            this.set = set;
            this.furniture = furniture;
            this.category = category;
        }
    }

    public class SetDetailDataFurniture {
        @SerializedName("id")
        private int id;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public SetDetailDataFurniture(int id, int category_id, String name, String size, String description, String image_url, String image_url_preview, boolean is_liked) {
            this.id = id;
            this.category_id = category_id;
            this.name = name;
            this.size = size;
            this.description = description;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
            this.is_liked = is_liked;
        }
    }

    public class SetDetailDataSet {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("amount")
        private int amount;

        public SetDetailDataSet(int id, String name, int amount) {
            this.id = id;
            this.name = name;
            this.amount = amount;
        }

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

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
