package com.example.library.dao.interfaces;

import com.example.library.entity.Notification;
import com.example.library.exception.DataAccessException;
import com.example.library.exception.DataNotFoundException;

import java.util.List;

public interface INotificationDAO {

    List<Notification> showNotifications(Long userID) throws DataNotFoundException;

    void createNotification(Notification notification) throws DataAccessException;

    void markAsRead(Long notificationID) throws DataAccessException;
}
