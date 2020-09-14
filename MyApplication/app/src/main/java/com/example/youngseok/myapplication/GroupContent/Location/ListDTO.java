package com.example.youngseok.myapplication.GroupContent.Location;

public class ListDTO {


    private String title;
    private String sub_title;
    private String lat;
    private String lng;
    private String type_code;

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }


    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getSub_title() {
        return sub_title;
    }
}
