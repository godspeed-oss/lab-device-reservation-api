package com.lab.reservation.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.reservation.common.Result;
import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    public AuthInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            writeUnauthorizedResponse(response, "Missing token");
            return false;
        }

        CurrentUser currentUser = TokenUtil.parseToken(token);

        if (currentUser == null) {
            writeUnauthorizedResponse(response, "Invalid token");
            return false;
        }

        request.setAttribute("currentUser", currentUser);
        return true;
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(message)));
    }
}