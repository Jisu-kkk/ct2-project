package com.example.ct2.service;

import com.example.ct2.repo.ProjectMapper;
import com.example.ct2.service.Common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private CommonService commonService;

    public List<Map<String, Object>> selectProjectList(Map<String, Object> org) {
        List<Map<String, Object>> projectList = projectMapper.selectProjectList(org);

        for (Map<String, Object> param : projectList) {
            param.put("tagType", "BO001");

            List<Map<String, Object>> selectTagList = commonService.selectTagList(param);
            String tagList = "";

            for (int i = 0; i < selectTagList.size(); i++) {
                tagList += selectTagList.get(i).get("tagName");

                if (i != selectTagList.size() - 1) {
                    tagList += ", ";
                }
            }
            param.put("tagList", tagList);
        }
        return projectList;
    }
}
