package com.app.tution.items;

public class Conv {

    public String seen;
    public String timestamp;

    public Conv(){}

    public String isSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Conv(String seen, String timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}