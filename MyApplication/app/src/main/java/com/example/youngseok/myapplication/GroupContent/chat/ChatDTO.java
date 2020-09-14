package com.example.youngseok.myapplication.GroupContent.chat;

public class ChatDTO {

    private String userName;
    private String message;
    private String now_time;
    private String noti_flag;

    public ChatDTO() {}
    public ChatDTO(String userName, String message, String now_time,String noti_flag) {
        this.userName = userName;
        this.message = message;
        this.now_time=now_time;
        this.noti_flag=noti_flag;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setNow_time(String now_time){
        this.now_time = now_time;
    }
    public void setNoti_flag(String noti_flag){
        this.noti_flag=noti_flag;
    }
    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
    public String getNow_time(){
        return now_time;
    }
    public String getNoti_flag(){
        return noti_flag;
    }
}