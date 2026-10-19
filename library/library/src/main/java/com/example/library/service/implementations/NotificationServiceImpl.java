package com.example.library.service.implementations;

import com.example.library.dao.interfaces.INotificationDAO;
import com.example.library.entity.Notification;
import com.example.library.service.interfaces.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final INotificationDAO notifDAO;

    public NotificationServiceImpl(INotificationDAO notifDAO){
        this.notifDAO = notifDAO;
    }

    @Override
    public List<Notification> getUserNotifications(Long userID) {
        return notifDAO.showNotifications(userID);
    }

    @Override
    public void markAsRead(Long notificationID) {
        notifDAO.markAsRead(notificationID);
    }

    @Override
    public int getUnreadCount(Long userID) {

        return (int) notifDAO
                .showNotifications(userID)
                .stream()
                .filter(notification -> !notification.getRead())
                .count();
    }
}
