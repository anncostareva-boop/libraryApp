package com.example.library.entity;

import java.sql.Date;

public class Notification {
    private Long notificationID;
    private Long receiverID;

    private String title;
    private String message;
    private Date createdAt;
    private Boolean isRead;

    public Notification(){}
    public Notification(Long notificationID, Long receiverID, String title,
                        String message, Date createdAt) {
        this.notificationID = notificationID;
        this.receiverID = receiverID;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Long getNotificationID() { return notificationID; }
    public void setNotificationID(Long notificationID) { this.notificationID = notificationID; }

    public Long getReceiverID() { return receiverID; }
    public void setReceiverID(Long receiverID) { this.receiverID = receiverID; }

    public Boolean getRead() { return isRead; }
    public void setRead(Boolean read) { isRead = read; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
