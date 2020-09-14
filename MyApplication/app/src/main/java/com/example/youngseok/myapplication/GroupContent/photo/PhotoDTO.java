package com.example.youngseok.myapplication.GroupContent.photo;

import java.io.Serializable;

public class PhotoDTO implements Serializable {

    private String master_key;
    private String financial_dialog_picture;


    public String getMaster_key() {
        return master_key;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public String getFinancial_dialog_picture() {
        return financial_dialog_picture;
    }

    public void setFinancial_dialog_picture(String financial_dialog_picture) {
        this.financial_dialog_picture = financial_dialog_picture;
    }

}
