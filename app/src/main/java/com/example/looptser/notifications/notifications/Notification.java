package com.example.looptser.notifications.notifications;

public class Notification {
    private String userName, userUid, time;

    public Notification(String userName, String userUid, String time) {
        this.userName = userName;
        this.userUid = userUid;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
