package com.app.tution.items;

import java.util.Date;

public class PostClass {
    String UID;
    String title, description;
    int likes;
    String owner;
    Date date;
    String ownerUID;

    public PostClass() {
    }

    public PostClass(String UID, String title, String description, String owner, Date date, String ownerUID) {
        this.UID = UID;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.date = date;
        likes = 0;
        this.ownerUID = ownerUID;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
