package com.example.ct2.admin.common.service;

import com.example.ct2.admin.common.mapper.UserMapper;
import com.example.ct2.admin.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserVo selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    public UserVo selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
