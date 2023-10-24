package com.example.ct2.admin.intro.service;

import com.example.ct2.admin.intro.mapper.IntroMngMapper;
import com.example.ct2.admin.common.service.S3FileService;
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
    private S3FileService s3Service;

    private String introFolder = "intro/";

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
        //int thumbnailNo = fileService.insertFile(thumbnail);
        int thumbnailNo = s3Service.insertFile(thumbnail, introFolder);

        if (thumbnailNo > 0) {
            param.put("thumbnailNo", thumbnailNo);
            result = introMngMapper.insertIntro(param);
        }
        return result;
    }

    @Transactional
    public int updateIntro(Map<String, Object> param) {
        int result = -1;

        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail");

        //int updateThumbnail = fileService.updateFile(thumbnail, Integer.parseInt((String) param.get("file_id")));
        int updateThumbnail = s3Service.updateFile(thumbnail, introFolder, Integer.parseInt((String) param.get("file_id")));
        param.put("file_id", updateThumbnail);
        if(updateThumbnail > 0) {
            result = introMngMapper.updateIntro(param);
        }
        return result;
    }

    @Transactional
    public int deleteIntro(Map<String, Object> param) {
        int result = -1;

        Long introId = (Long) param.get("introId");
        if (introId == null) {
            param.put("introId", param.get("id"));
        }

        int deleteIntro = introMngMapper.deleteIntro(param);
        if (deleteIntro > 0) {
            //fileService.deleteFile((Long) param.get("file_id"));
            s3Service.deleteFile((Long) param.get("file_id"));
        }
        return result;
    }
}