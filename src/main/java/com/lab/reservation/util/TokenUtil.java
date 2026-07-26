package com.lab.reservation.util;

import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.entity.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class TokenUtil {
    private TokenUtil() {
    }

    public static String generateToken(User user) {
        String rawToken = user.getId()
                + ":"
                + user.getUsername()
                + ":"
                + user.getRole()
                + ":"
                + UUID.randomUUID();

        return Base64.getEncoder().encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));
    }

    public static CurrentUser parseToken(String token) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            String rawToken = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = rawToken.split(":");

            if (parts.length < 4) {
                return null;
            }

            Integer userId = Integer.valueOf(parts[0]);
            String username = parts[1];
            String role = parts[2];

            return new CurrentUser(userId, username, role);
        } catch (Exception exception) {
            return null;
        }
    }
}