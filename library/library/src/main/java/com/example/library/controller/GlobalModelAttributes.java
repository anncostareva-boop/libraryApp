package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.service.interfaces.NotificationService;
import com.example.library.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final NotificationService notificationService;
    private final UserService userService;

    public GlobalModelAttributes(
            NotificationService notificationService,
            UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @ModelAttribute("unreadNotifications")
    public int unreadNotifications(Authentication auth) {
        // Якщо користувач взагалі не залогінений
        if (auth == null || !auth.isAuthenticated()) {
            return 0;
        }

        try {
            // Дістаємо поточного користувача за його унікальним логіном
            User currentUser = userService.getByUsername(auth.getName());

            if (currentUser == null) {
                return 0;
            }

            return notificationService.getUnreadCount(currentUser.getUserID());

        } catch (Exception e) {
            return 0;
        }
    }
}