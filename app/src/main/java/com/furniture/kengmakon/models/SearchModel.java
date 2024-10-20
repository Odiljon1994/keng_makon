package com.furniture.kengmakon.models;

public class SearchModel {
    private int furniture_id;
    private String name;
    private String image_url;

    public int getFurniture_id() {
        return furniture_id;
    }

    public void setFurniture_id(int furniture_id) {
        this.furniture_id = furniture_id;
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

    public SearchModel(int furniture_id, String name, String image_url) {
        this.furniture_id = furniture_id;
        this.name = name;
        this.image_url = image_url;
    }
}
