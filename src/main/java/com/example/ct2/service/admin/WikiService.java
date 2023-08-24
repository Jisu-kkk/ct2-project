package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.WikiMapper;
import com.example.ct2.service.Common.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
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

    public Map<String, Object> selectWiki(int wikiId) {
        return wikiMapper.selectWiki(wikiId);
    }

    public List<Integer> selectWikiTagList(int wikiId) {
        return wikiMapper.selectWikiTagList(wikiId);
    }

    @Transactional
    public int updateWiki(Map<String, Object> param) {
        int result = -1;
        int updateWiki = -1;

        Map<String, Object> selectWiki = selectWiki(Integer.parseInt((String)param.get("wikiId")));
        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");
        MultipartFile titleImg = (MultipartFile) param.get("title_img");

        int updateThumbnail = fileService.updateFile(thumbnail, ((Long)selectWiki.get("thumbnailNo")).intValue());
        int updateTitleImg = fileService.updateFile(titleImg, ((Long)selectWiki.get("thumbnailNo")).intValue());

        // 이미지 수정
        if (updateThumbnail > 0 && updateTitleImg > 0) {
            // wiki 수정
            updateWiki = wikiMapper.updateWiki(param);
        }

        if (updateWiki > 0) {
            // tag 삭제
            int deleteWikiTag = wikiMapper.deleteWikiTag(param);
            // tag 넣기
            if (deleteWikiTag > 0) {
                String[] tagList = ((String)param.get("hashTag")).split(",");
                for (String tag : tagList) {
                    param.put("tag_id", tag);
                    param.put("board_id", param.get("wikiId"));
                    wikiMapper.insertWikiTag(param);
                }
            }
        }


        return result;
    }
}
