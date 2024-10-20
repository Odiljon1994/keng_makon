package com.furniture.kengmakon.models;

import android.graphics.drawable.Drawable;

public class GetStartedModel {

    private Drawable drawable;
    private String title;
    private String subtitle;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public GetStartedModel(Drawable drawable, String title, String subtitle) {
        this.drawable = drawable;
        this.title = title;
        this.subtitle = subtitle;
    }
}
