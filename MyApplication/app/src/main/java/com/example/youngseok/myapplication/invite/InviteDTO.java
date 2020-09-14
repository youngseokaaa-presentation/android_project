package com.example.youngseok.myapplication.invite;

import java.io.Serializable;

public class InviteDTO implements Serializable {

    private String phonebook_name;
    private String phonebook_phone;
    private int count =0;
    private String check_box_key;
    private String id;

    public String getPhonebook_name() {
        return phonebook_name;
    }

    public String getPhonebook_phone(){
        return phonebook_phone;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPhonebook_name(String phonebook_name) {
        this.phonebook_name = phonebook_name;
    }

    public void setPhonebook_phone(String phonebook_phone) {
        this.phonebook_phone = phonebook_phone;
    }

    public String getCheck_box_key() {
        return check_box_key;
    }

    public void setCheck_box_key(String check_box_key) {
        this.check_box_key = check_box_key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
