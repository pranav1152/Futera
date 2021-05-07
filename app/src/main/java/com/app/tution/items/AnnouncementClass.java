package com.app.tution.items;

import java.util.Date;

public class AnnouncementClass {
    Date date;
    String des;

    public AnnouncementClass() {
    }

    public AnnouncementClass(Date date, String des) {
        this.date = date;
        this.des = des;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
