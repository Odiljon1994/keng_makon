package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FurnitureDetailModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private FurnitureDetailData data;

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

    public FurnitureDetailData getData() {
        return data;
    }

    public void setData(FurnitureDetailData data) {
        this.data = data;
    }

    public FurnitureDetailModel(int code, String message, FurnitureDetailData data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public class FurnitureDetailData {
        @SerializedName("furniture")
        private DetailDataFurniture furniture;
        @SerializedName("images")
        private List<FurnitureImages> images;

        public DetailDataFurniture getFurniture() {
            return furniture;
        }

        public void setFurniture(DetailDataFurniture furniture) {
            this.furniture = furniture;
        }

        public List<FurnitureImages> getImages() {
            return images;
        }

        public void setImages(List<FurnitureImages> images) {
            this.images = images;
        }

        public FurnitureDetailData(DetailDataFurniture furniture, List<FurnitureImages> images) {
            this.furniture = furniture;
            this.images = images;
        }
    }

    public class FurnitureImages {
        @SerializedName("media_id")
        private int media_id;
        @SerializedName("image_url")
        private String image_url;
        @SerializedName("image_url_preview")
        private String image_url_preview;

        public int getMedia_id() {
            return media_id;
        }

        public void setMedia_id(int media_id) {
            this.media_id = media_id;
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

        public FurnitureImages(int media_id, String image_url, String image_url_preview) {
            this.media_id = media_id;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
        }
    }

    public class DetailDataFurniture {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("image_url")
        private String image_url;
        @SerializedName("image_url_preview")
        private String image_url_preview;
        @SerializedName("category")
        private FurnitureModel.FurnitureDataItemCategory category;
        @SerializedName("info")
        private FurnitureInfo info;
        @SerializedName("is_liked")
        private boolean is_liked;

        public DetailDataFurniture(int id, String name, String image_url, String image_url_preview, FurnitureModel.FurnitureDataItemCategory category, FurnitureInfo info, boolean is_liked) {
            this.id = id;
            this.name = name;
            this.image_url = image_url;
            this.image_url_preview = image_url_preview;
            this.category = category;
            this.info = info;
            this.is_liked = is_liked;
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

        public FurnitureModel.FurnitureDataItemCategory getCategory() {
            return category;
        }

        public void setCategory(FurnitureModel.FurnitureDataItemCategory category) {
            this.category = category;
        }

        public FurnitureInfo getInfo() {
            return info;
        }

        public void setInfo(FurnitureInfo info) {
            this.info = info;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }
    }

    public class FurnitureInfo {
        @SerializedName("description")
        private String description;
        @SerializedName("materials")
        private String materials;
        @SerializedName("handmade")
        private String handmade;
        @SerializedName("size")
        private String size;
        @SerializedName("delivery")
        private String delivery;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMaterials() {
            return materials;
        }

        public void setMaterials(String materials) {
            this.materials = materials;
        }

        public String getHandmade() {
            return handmade;
        }

        public void setHandmade(String handmade) {
            this.handmade = handmade;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public FurnitureInfo(String description, String materials, String handmade, String size, String delivery) {
            this.description = description;
            this.materials = materials;
            this.handmade = handmade;
            this.size = size;
            this.delivery = delivery;
        }
    }
}
