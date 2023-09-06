package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.ProjectMngMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectMngService {

    @Autowired
    private ProjectMngMapper projectMngMapper;

    public int selectProjectListCnt(Map<String, Object> param) {
        return projectMngMapper.selectProjectListCnt(param);
    }

    public List<Map<String, Object>> selectProjectList(Map<String, Object> param) {
        return projectMngMapper.selectProjectList(param);
    }

}
