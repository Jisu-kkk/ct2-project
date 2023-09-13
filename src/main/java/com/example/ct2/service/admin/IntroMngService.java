package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.IntroMngMapper;
import com.example.ct2.service.Common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntroMngService {

    @Autowired
    private IntroMngMapper introMngMapper;

    @Autowired
    private FileService fileService;

    public List<Map<String, Object>> selectIntroList(Map<String, Object> org) {
        return introMngMapper.selectIntroList(org);
    }

    public Map<String, Object> selectIntro(Map<String, Object> param) {
        return introMngMapper.selectIntro(param);
    }

    @Transactional
    public int updateIntroDepth(List<String> depthList) {
        int result = -1;
        for (int i = 0; i < depthList.size(); i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("depth", (i + 1));
            param.put("id", depthList.get(i));

            introMngMapper.updateIntroDepth(param);
        }
        return result;
    }

    @Transactional
    public int insertIntro(Map<String, Object> param) {
        int result = -1;

        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");
        int thumbnailNo = fileService.insertFile(thumbnail);

        if (thumbnailNo > 0) {
            param.put("thumbnailNo", thumbnailNo);
            result = introMngMapper.insertIntro(param);
        }
        return result;
    }
}