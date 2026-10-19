package com.example.library.entity;

import com.example.library.enums.Role;

public class User {

    private Long userID;
    private String userName;
    private String email;
    private String password;

    private Role role;
    //private Boolean blocked;

    public User() {}
    public User(Long userID, String userName, String email,
                String password, Role role) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
        //this.blocked = false;
    }

    public Long getUserID() { return userID; }
    public void setUserID(Long userID) { this.userID = userID; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    //public Boolean getBlocked() { return blocked; }
    //public void setBlocked(Boolean blocked) { this.blocked = blocked; }
}
