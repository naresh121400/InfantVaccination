package com.example.admin.infantvaccination;

/**
 * Created by Admin on 09-Mar-18.
 */

public class NotificationsModelClass {

    String cid;
    String notificationId;
    String notificationDate;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
