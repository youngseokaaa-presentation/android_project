package com.example.youngseok.myapplication.setting;

public class SettingDTO {

    private String id;
    private String nickname;
    private String phone;
    private String picture;

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getPicture() {
        return picture;
    }
}
