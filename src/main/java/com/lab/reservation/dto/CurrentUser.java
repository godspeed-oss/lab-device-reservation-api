package com.lab.reservation.dto;

import lombok.Data;

@Data
public class CurrentUser {
    private Integer userId;
    private String username;
    private String role;

    public CurrentUser(Integer userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}