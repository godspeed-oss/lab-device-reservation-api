package com.lab.reservation.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Integer userId;
    private String username;
    private String role;
    private String token;

    public LoginResponse(Integer userId, String username, String role, String token) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.token = token;
    }
}