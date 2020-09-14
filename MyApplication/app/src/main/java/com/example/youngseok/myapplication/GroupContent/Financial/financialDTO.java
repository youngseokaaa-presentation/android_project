package com.example.youngseok.myapplication.GroupContent.Financial;

public class financialDTO {

    private String master_key;
    private String account;
    private String money_type;
    private String money;
    private String money_explain;
    private String account_time;
    private String bank_or_hand;
    private String content_edit;
    private String result;

    public financialDTO(String master_key, String account, String money_type, String money, String money_explain, String account_time, String bank_or_hand, String content_edit, String result) {
        this.master_key = master_key;
        this.account = account;
        this.money_type = money_type;
        this.money = money;
        this.money_explain = money_explain;
        this.account_time = account_time;
        this.bank_or_hand = bank_or_hand;
        this.content_edit = content_edit;
        this.result = result;
    }

    public String getMaster_key() {
        return master_key;
    }

    public String getAccount() {
        return account;
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

    public String getBank_or_hand() {
        return bank_or_hand;
    }

    public String getContent_edit() {
        return content_edit;
    }

    public String getResult() {
        return result;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public void setBank_or_hand(String bank_or_hand) {
        this.bank_or_hand = bank_or_hand;
    }

    public void setContent_edit(String content_edit) {
        this.content_edit = content_edit;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
