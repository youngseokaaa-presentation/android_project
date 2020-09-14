package com.example.youngseok.myapplication.invite;

public class InviteDTO_confirm {

    private String id;
    private String name;
    private String content;
    private String sumnail;
    private String profile;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getSumnail() {
        return sumnail;
    }

    public String getProfile() {
        return profile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSumnail(String sumnail) {
        this.sumnail = sumnail;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
