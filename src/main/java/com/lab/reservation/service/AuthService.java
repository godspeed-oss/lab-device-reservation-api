package com.lab.reservation.service;

import com.lab.reservation.dto.LoginRequest;
import com.lab.reservation.dto.LoginResponse;
import com.lab.reservation.entity.User;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.mapper.UserMapper;
import com.lab.reservation.util.PasswordUtil;
import com.lab.reservation.util.TokenUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());

        if (user == null) {
            throw new BusinessException("Username or password is incorrect");
        }

        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Username or password is incorrect");
        }

        String token = TokenUtil.generateToken(user);

        return new LoginResponse(user.getId(), user.getUsername(), user.getRole(), token);
    }
}