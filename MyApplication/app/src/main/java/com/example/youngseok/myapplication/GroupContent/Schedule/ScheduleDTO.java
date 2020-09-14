package com.example.youngseok.myapplication.GroupContent.Schedule;

import java.io.Serializable;

public class ScheduleDTO implements Serializable {
    private int year;
    private int month;
    private int day;
    private String name;
    private String master_key;
    private String schedule_content;

    private String time_hour;
    private String time_minute;
    private String schedule_content_detail;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getMaster_key() {
        return master_key;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaster_key(String master_key) {
        this.master_key = master_key;
    }

    public String getSchedule_content() {
        return schedule_content;
    }

    public void setSchedule_content(String schedule_content) {
        this.schedule_content = schedule_content;
    }

    public String getSchedule_content_detail() {
        return schedule_content_detail;
    }


    public void setSchedule_content_detail(String schedule_content_detail) {
        this.schedule_content_detail = schedule_content_detail;
    }

    public String getTime_hour() {
        return time_hour;
    }

    public String getTime_minute() {
        return time_minute;
    }

    public void setTime_hour(String time_hour) {
        this.time_hour = time_hour;
    }

    public void setTime_minute(String time_minute) {
        this.time_minute = time_minute;
    }
}
