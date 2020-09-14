package com.example.youngseok.myapplication.make_group;

public class basicGroup {

    private String group_name;
    private String group_content;
    private String group_sumnail;
    private String group_picture;
    private String master_key;


    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_picture(){
        return group_picture;
    }
    public void setGroup_picture(String group_picture){
        this.group_picture="http://192.168.43.34/uploads/"+group_picture;
    }

    public String getGroup_content() {
        return group_content;
    }

    public void setGroup_content(String group_content) {
        this.group_content = group_content;
    }

    public String getGroup_sumnail() {
        return group_sumnail;
    }

    public void setGroup_sumnail(String group_sumnail) {
        this.group_sumnail = group_sumnail;
    }

    public String getMaster_key() {
        return master_key;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }
}
