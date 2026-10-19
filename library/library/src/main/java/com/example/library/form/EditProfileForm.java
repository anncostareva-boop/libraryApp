package com.example.library.form;

import jakarta.validation.constraints.NotBlank;

public class EditProfileForm {
    @NotBlank(message = "Введіть ім'я користувача")
    private String username;
    @NotBlank(message = "Введіть електронну адресу")
    private String email;

    public EditProfileForm() {}

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
}
