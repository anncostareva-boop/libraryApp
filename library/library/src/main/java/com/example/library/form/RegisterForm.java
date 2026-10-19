package com.example.library.form;

import jakarta.validation.constraints.*;


public class RegisterForm {
    @NotBlank(message = "Введіть ім'я користувача")
    private String username;

    @NotBlank(message = "Введіть свою електронну пошту")
    private String email;

    @NotBlank(message = "Введіть пароль")
    private String password;

    @NotBlank(message = "Повторіть пароль")
    private String confirmPassword;

    public RegisterForm() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
