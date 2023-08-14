package com.example.ct2.service;

import com.example.ct2.repo.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrganizationService {

    @Autowired
    OrganizationMapper orgMapper;

    public Map<String, Object> selectOrg(String type) {
        return orgMapper.selectOrg(type);
    }
}
