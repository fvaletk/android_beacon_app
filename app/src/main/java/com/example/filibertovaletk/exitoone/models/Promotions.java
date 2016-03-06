package com.example.filibertovaletk.exitoone.models;

/**
 * Created by filibertovaletk on 3/5/16.
 */
public class Promotions {

    private String title;
    private String description;
    private String date;
    private String image_url;

    public Promotions(){}

    public Promotions(String title, String description, String date, String image_url){
        this.title = title;
        this.description = description;
        this.date = date;
        this.image_url = image_url;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
