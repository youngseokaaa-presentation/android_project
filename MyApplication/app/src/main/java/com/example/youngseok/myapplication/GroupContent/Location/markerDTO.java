package com.example.youngseok.myapplication.GroupContent.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class markerDTO implements ClusterItem {
    private String title;
    private String snip;
    private String location_lat;
    private String location_lng;



    private LatLng mPosition;

    public markerDTO(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }
    public markerDTO(double lat, double lng,String mtitle,String msnippet) {
        mPosition = new LatLng(lat, lng);
        title=mtitle;
        snip=msnippet;
    }

    public markerDTO() {

    }
    public LatLng getmPosition() {
        return mPosition;
    }

    public void setmPosition(String location_lat,String location_lng) {
        this.mPosition = new LatLng(Double.valueOf(location_lat),Double.valueOf(location_lng));
    }

    public void setLocation_lng(String location_lng) {
        this.location_lng = location_lng;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public void setSnip(String snip) {
        this.snip = snip;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation_lng() {
        return location_lng;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public String getSnip() {
        return snip;
    }

    public String getTitle() {
        return title;
    }
    @Override
    public LatLng getPosition() {
        return mPosition;
    }



    @Override
    public String getSnippet() {
        return snip;
    }
}
