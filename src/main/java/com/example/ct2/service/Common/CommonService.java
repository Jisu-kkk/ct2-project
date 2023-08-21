package com.example.ct2.service.Common;

import com.example.ct2.repo.Common.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private CommonMapper commonMapper;

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

}
