package com.example.youngseok.myapplication.GroupContent.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class sharing_locationDTO  {
    private String master_key;
    private String name;
    private String time;
    private String location_lat;
    private String location_lng;




    public void setName(String name) {
        this.name = name;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public String getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public void setLocation_lng(String location_lng) {
        this.location_lng = location_lng;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }



    public String getMaster_key() {
        return master_key;
    }

    public String getTime() {
        return time;
    }



}
