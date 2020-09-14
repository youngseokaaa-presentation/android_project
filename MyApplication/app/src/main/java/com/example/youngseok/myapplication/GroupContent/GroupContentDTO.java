package com.example.youngseok.myapplication.GroupContent;

import java.io.Serializable;

public class GroupContentDTO implements Serializable {

    private String group_id;
    private String group_name;
    private String group_joiner;

    public String getGroup_id() {
        return group_id;
    }

    public String getGroup_joiner() {
        return group_joiner;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setGroup_joiner(String group_joiner) {
        this.group_joiner = group_joiner;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
