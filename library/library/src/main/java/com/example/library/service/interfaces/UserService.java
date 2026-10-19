package com.example.library.service.interfaces;

import com.example.library.entity.User;

import java.util.List;

public interface UserService {
    // знаходження користувача за айді
    User getUserByID(Long userID);

    // знаходження користувача за юзернеймом
    User getByUsername(String username);

    // редагування профілю
    void editProfile(User user);

    List<User> getAllUsers();

}
