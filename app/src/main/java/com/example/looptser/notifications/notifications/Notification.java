package com.example.looptser.notifications.notifications;

public class Notification {
    private String userName, userUid, time, userProfile;

    public Notification() {}

    public Notification(String userName, String userProfile, String userUid, String time) {
        this.userName = userName;
        this.userUid = userUid;
        this.time = time;
        this.userProfile = userProfile;
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

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
