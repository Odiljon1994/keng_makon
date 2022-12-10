package com.toplevel.kengmakon.models;

import com.google.gson.annotations.SerializedName;

public class FeedbackModel {
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("subject")
    private String subject;
    @SerializedName("message")
    private String message;
    @SerializedName("score")
    private float score;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public FeedbackModel(int category_id, String subject, String message, float score) {
        this.category_id = category_id;
        this.subject = subject;
        this.message = message;
        this.score = score;
    }
}
