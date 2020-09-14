package com.example.youngseok.myapplication.GroupContent.Location;

public class custom_marker_DTO {
    double lat;
    double lng;
    String name;
    String time;


    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public custom_marker_DTO(double lat, double lng, String name, String time){
        this.lat=lat;
        this.lng=lng;
        this.name=name;
        this.time=time;
    }
}
