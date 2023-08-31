package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.IntroMngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IntroMngService {

    @Autowired
    private IntroMngMapper introMngMapper;

    public List<Map<String, Object>> selectIntroList(Map<String, Object> org) {
        return introMngMapper.selectIntroList(org);
    }

}
