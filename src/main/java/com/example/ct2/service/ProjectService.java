package com.example.ct2.service;

import com.example.ct2.repo.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    public List<Map<String, Object>> selectProjectList(Map<String, Object> org) {
        List<Map<String, Object>> projectList = projectMapper.selectProjectList(org);

        for (Map<String, Object> param : projectList) {
            List<Map<String, Object>> selectProjectTag = projectMapper.selectProjectTag(param);
            String tagList = "";

            for (int i = 0; i < selectProjectTag.size(); i++) {
                tagList += selectProjectTag.get(i).get("tagName");
                if (i != selectProjectTag.size() - 1) {
                    tagList += ", ";
                }
            }
            param.put("tagList", tagList);
        }
        return projectList;
    }
}
