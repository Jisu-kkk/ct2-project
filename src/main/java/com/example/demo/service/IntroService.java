package com.example.demo.service;

import com.example.demo.repo.IntroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IntroService {

    @Autowired
    private IntroMapper introMapper;

    public List<Map<String, Object>> selectIntroList(Map<String, Object> org) {
        return introMapper.selectIntroList(org);
    }
}
