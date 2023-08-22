package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.WikiMapper;
import com.example.ct2.service.Common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WikiService {

    @Autowired
    private WikiMapper wikiMapper;

    @Autowired
    private FileService fileService;

    public int selectWikiListCnt(Map<String, Object> param) {
        return wikiMapper.selectWikiListCnt(param);
    }

    public List<Map<String, Object>> selectWikiList(Map<String, Object> param) {
        List<Map<String, Object>> selectWikiList = wikiMapper.selectWikiList(param);
        return selectWikiList;
    }

    public int insertWiki(Map<String, Object> param) {
        int result = -1;
        int insertWiki = -1;

        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");
        MultipartFile titleImg = (MultipartFile) param.get("title_img");

        int titleImgNo = fileService.insertFile(titleImg);
        int thumbnailNo = fileService.insertFile(thumbnail);

        if (thumbnailNo > 0 && titleImgNo > 0) {
            param.put("titleImgNo", titleImgNo);
            param.put("thumbnailNo", thumbnailNo);

            insertWiki = wikiMapper.insertWiki(param);
        }

        if (insertWiki > 0) {
            String[] tagList = ((String)param.get("hashTag")).split(",");

            for(String tag : tagList) {
                param.put("tag_id", tag);
                wikiMapper.insertWikiTag(param);
            }
        }

        return result;
    }
}
