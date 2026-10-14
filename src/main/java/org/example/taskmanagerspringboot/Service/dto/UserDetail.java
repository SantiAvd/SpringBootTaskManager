package org.example.taskmanagerspringboot.Service.dto;

import org.example.taskmanagerspringboot.model.UserRole;

public class UserDetail {
    private String username;
    private UserRole role;
    private String password;

    public UserDetail(String username, UserRole role, String password) {
        this.username = username;
        this.role = role;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
