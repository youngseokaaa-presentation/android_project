package com.example.youngseok.myapplication.Service;

public class Wait_member_DTO {

    private String master_key;
    private String master_id;
    private String joiner;
    private String name;

    public String getMaster_key() {
        return master_key;
    }

    public String getJoiner() {
        return joiner;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public void setJoiner(String joiner) {
        this.joiner = joiner;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
