package com.lab.reservation.util;

import com.lab.reservation.dto.CurrentUser;
import com.lab.reservation.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {
    @Test
    void shouldGenerateAndParseToken() {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setRole("ADMIN");

        String token = TokenUtil.generateToken(user);
        CurrentUser currentUser = TokenUtil.parseToken(token);

        assertNotNull(token);
        assertTrue(token.contains("."));
        assertNotNull(currentUser);
        assertEquals(1, currentUser.getUserId());
        assertEquals("admin", currentUser.getUsername());
        assertEquals("ADMIN", currentUser.getRole());
    }

    @Test
    void shouldReturnNullWhenTokenIsInvalid() {
        CurrentUser currentUser = TokenUtil.parseToken("invalid-token");

        assertNull(currentUser);
    }

    @Test
    void shouldReturnNullWhenTokenIsTampered() {
        User user = new User();
        user.setId(2);
        user.setUsername("student");
        user.setRole("USER");

        String token = TokenUtil.generateToken(user);
        String tamperedToken = token.substring(0, token.length() - 1) + "x";

        CurrentUser currentUser = TokenUtil.parseToken(tamperedToken);

        assertNull(currentUser);
    }
}