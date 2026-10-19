package com.example.library.service.interfaces;

import com.example.library.entity.Notification;

import java.util.List;

public interface NotificationService {
    // показує повідомлення користувача
    List<Notification> getUserNotifications(Long userID);

    //змінює статус повідомлення на "прочитано"
    void markAsRead(Long notificationID);

    int getUnreadCount(Long userID);
}
