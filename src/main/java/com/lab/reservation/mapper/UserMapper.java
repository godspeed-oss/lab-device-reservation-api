package com.lab.reservation.mapper;

import com.lab.reservation.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT id, username, password, role FROM user WHERE username = #{username}")
    User findByUsername(String username);
}