package com.example.demo.service;

import com.example.demo.repo.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public ArrayList<Map<String, Object>> selectList() {
        return userMapper.selectList();
    }
}
