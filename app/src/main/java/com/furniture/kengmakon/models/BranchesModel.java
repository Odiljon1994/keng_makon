package com.furniture.kengmakon.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchesModel {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<BranchesData> data;

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

    public List<BranchesData> getData() {
        return data;
    }

    public void setData(List<BranchesData> data) {
        this.data = data;
    }

    public BranchesModel(int code, String message, List<BranchesData> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public class BranchesData {
        @SerializedName("store")
        private BranchesStore store;
        @SerializedName("region")
        private BranchesRegion region;

        public BranchesStore getStore() {
            return store;
        }

        public void setStore(BranchesStore store) {
            this.store = store;
        }

        public BranchesRegion getRegion() {
            return region;
        }

        public void setRegion(BranchesRegion region) {
            this.region = region;
        }

        public BranchesData(BranchesStore store, BranchesRegion region) {
            this.store = store;
            this.region = region;
        }
    }

    public class BranchesRegion {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BranchesRegion(String name) {
            this.name = name;
        }
    }

    public class BranchesStore {
        @SerializedName("name")
        private String name;
        @SerializedName("has_parking")
        private int has_parking;
        @SerializedName("open_hours")
        private String open_hours;
        @SerializedName("address")
        private String address;
        @SerializedName("landmark")
        private String landmark;
        @SerializedName("lat_long")
        private String lat_long;
        @SerializedName("images")
        private List<String> images;
        @SerializedName("phone")
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHas_parking() {
            return has_parking;
        }

        public void setHas_parking(int has_parking) {
            this.has_parking = has_parking;
        }

        public String getOpen_hours() {
            return open_hours;
        }

        public void setOpen_hours(String open_hours) {
            this.open_hours = open_hours;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getLat_long() {
            return lat_long;
        }

        public void setLat_long(String lat_long) {
            this.lat_long = lat_long;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public BranchesStore(String name, int has_parking, String open_hours, String address, String landmark, String lat_long, List<String> images, String phone) {
            this.name = name;
            this.has_parking = has_parking;
            this.open_hours = open_hours;
            this.address = address;
            this.landmark = landmark;
            this.lat_long = lat_long;
            this.images = images;
            this.phone = phone;
        }
    }
}
