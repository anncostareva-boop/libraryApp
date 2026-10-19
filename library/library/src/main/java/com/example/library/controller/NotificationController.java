package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.service.interfaces.NotificationService;
import com.example.library.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService
            notificationService;

    private final UserService
            userService;

    public NotificationController(

            NotificationService
                    notificationService,

            UserService userService
    ) {

        this.notificationService =
                notificationService;

        this.userService =
                userService;
    }

    @GetMapping
    public String notifications(Authentication auth, Model model) {
        User user = userService.getByUsername(auth.getName());

        model.addAttribute("notifications",
                notificationService.getUserNotifications(user.getUserID())
        );

        return "notifications";
    }

    @PostMapping("/read/{id}")
    public String read(@PathVariable Long id) {
        notificationService.markAsRead(id);

        return "redirect:/notifications";
    }
}
