package com.example.youngseok.myapplication.GroupContent.Financial.Financial_dialog;

import java.io.Serializable;

public class Financial_dialog_DTO  implements Serializable {


    private String master_key;
    private String money_type;
    private String money;
    private String money_explain;
    private String account_time;

    private String financial_dialog_picture;

    public String getMaster_key() {
        return master_key;
    }

    public String getMoney_type() {
        return money_type;
    }

    public String getMoney() {
        return money;
    }

    public String getMoney_explain() {
        return money_explain;
    }

    public String getAccount_time() {
        return account_time;
    }

    public String getFinancial_dialog_picture() {
        return financial_dialog_picture;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public void setMoney_type(String money_type) {
        this.money_type = money_type;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setMoney_explain(String money_explain) {
        this.money_explain = money_explain;
    }

    public void setAccount_time(String account_time) {
        this.account_time = account_time;
    }

    public void setFinancial_dialog_picture(String financial_dialog_picture) {
        this.financial_dialog_picture = financial_dialog_picture;
    }
}

