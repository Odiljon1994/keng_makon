package com.toplevel.kengmakon.models;

public class RecentlyViewedModel {

    private int ID;
    private String furnitureId;
    private String url;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(String furnitureId) {
        this.furnitureId = furnitureId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RecentlyViewedModel(int ID, String furnitureId, String url) {
        this.ID = ID;
        this.furnitureId = furnitureId;
        this.url = url;
    }
}
