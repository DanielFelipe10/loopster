package com.example.looptser.notifications.notifications;

import java.util.ArrayList;

public class Notifications {
    private ArrayList<Notification> notificationsList;

    public Notifications(ArrayList<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public ArrayList<Notification> getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(ArrayList<Notification> notificationsList) {
        this.notificationsList = notificationsList;
    }
}
