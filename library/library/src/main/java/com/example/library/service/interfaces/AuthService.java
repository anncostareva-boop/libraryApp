package com.example.library.service.interfaces;

import com.example.library.entity.User;

public interface AuthService {
    // метод для реєстрації нового користувача
    void register(User user) throws Exception;

    boolean usernameExists(String username);

    boolean emailExists(String email);
}
