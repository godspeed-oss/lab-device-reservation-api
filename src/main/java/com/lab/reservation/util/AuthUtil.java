package com.lab.reservation.util;

import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {
    private AuthUtil() {
    }

    public static CurrentUser getCurrentUser(HttpServletRequest request) {
        Object currentUser = request.getAttribute("currentUser");

        if (!(currentUser instanceof CurrentUser)) {
            throw new BusinessException("Unauthorized");
        }

        return (CurrentUser) currentUser;
    }

    public static void requireAdmin(HttpServletRequest request) {
        CurrentUser currentUser = getCurrentUser(request);

        if (!"ADMIN".equals(currentUser.getRole())) {
            throw new BusinessException("Admin permission required");
        }
    }
}