package com.example.ct2.service.Common;

import com.example.ct2.repo.Common.CommonMapper;
import com.example.ct2.repo.admin.ProjectMngMapper;
import com.example.ct2.repo.admin.WikiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private WikiMapper wikiMapper;

    @Autowired
    private ProjectMngMapper projectMngMapper;

    public List<Map<String, Object>> selectTagList(Map<String, Object> param) {
        List<Map<String, Object>> tagList = new ArrayList<>();
        if (param.get("strTagIds") != null) {
            String[] strTagIds = ((String)param.get("strTagIds")).split(",");
            int[] tagIds = new int[strTagIds.length];

            for (int i = 0; i < strTagIds.length; i++) {
                tagIds[i] = Integer.parseInt(strTagIds[i]);
            }
            param.put("tagIds", tagIds);
        }
        tagList = commonMapper.selectTagList(param);
        return tagList;
    }

    public List<Map<String, Object>> selectOrgUserList(Map<String, Object> param) {
        return commonMapper.selectOrgUserList(param);
    }

    public int selectWikiListCnt(Map<String, Object> param, String useState) {
        param.put("showStatus", useState);
        return wikiMapper.selectWikiListCnt(param);
    }

    public int selectProjectListCnt(Map<String, Object> param, String useState) {
        param.put("showStatus", useState);
        return projectMngMapper.selectProjectListCnt(param);
    }

    public List<Map<String, Object>> selectOrgUserProfileList(Map<String, Object> param) {
        return commonMapper.selectOrgUserProfileList(param);
    }

    public Map<String, Object> selectOrgUserProfileOne(Map<String, Object> param) {
        return commonMapper.selectOrgUserProfileOne(param);
    }

}
