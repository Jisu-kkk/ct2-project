package com.example.ct2.admin.wiki.service;

import com.example.ct2.admin.wiki.mapper.WikiMngMapper;
import com.example.ct2.admin.common.service.S3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class WikiMngService {

    @Autowired
    private WikiMngMapper wikiMngMapper;

    @Autowired
    private S3FileService s3Service;

    private String wikiFolder = "wiki/";

    public int selectWikiListCnt(Map<String, Object> param) {
        return wikiMngMapper.selectWikiListCnt(param);
    }

    public List<Map<String, Object>> selectWikiList(Map<String, Object> param) {
        List<Map<String, Object>> selectWikiList = wikiMngMapper.selectWikiList(param);
        return selectWikiList;
    }

    @Transactional
    public int insertWiki(Map<String, Object> param) throws IOException {
        int result = -1;
        int insertWiki = -1;

        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");
        MultipartFile titleImg = (MultipartFile) param.get("title_img");

        int titleImgNo = s3Service.insertFile(titleImg, wikiFolder);
        int thumbnailNo = s3Service.insertFile(thumbnail, wikiFolder);

        if (titleImgNo > 0 && thumbnailNo > 0) {
            param.put("titleImgNo", titleImgNo);
            param.put("thumbnailNo", thumbnailNo);
            insertWiki = wikiMngMapper.insertWiki(param);
        }

        if (insertWiki > 0) {
            String[] tagList = ((String)param.get("hashTag")).split(",");

            for(String tag : tagList) {
                param.put("tag_id", tag);
                wikiMngMapper.insertWikiTag(param);
            }
            result = insertWiki;
        }

        return result;
    }

    public Map<String, Object> selectWiki(int wikiId) {
        return wikiMngMapper.selectWiki(wikiId);
    }

    public List<Integer> selectWikiTagList(int wikiId) {
        return wikiMngMapper.selectWikiTagList(wikiId);
    }

    @Transactional
    public int updateWiki(Map<String, Object> param) {
        int result = -1;
        int updateWiki = -1;

        Map<String, Object> selectWiki = selectWiki(Integer.parseInt((String)param.get("wikiId")));
        MultipartFile titleImg = (MultipartFile) param.get("title_img");
        MultipartFile thumbnail = (MultipartFile) param.get("thumbnail_img");

        // 대표이미지
        int updateTitleImg = s3Service.updateFile(titleImg, wikiFolder, ((Long)selectWiki.get("titleImgNo")).intValue());
        param.put("titleImgId", updateTitleImg);

        // 썸네일 이미지
        int updateThumbnail = s3Service.updateFile(thumbnail, wikiFolder, ((Long)selectWiki.get("thumbnailNo")).intValue());
        param.put("thumbImgId", updateThumbnail);

        // 이미지 수정
        if (updateThumbnail > 0 && updateTitleImg > 0) {
            // wiki 수정
            updateWiki = wikiMngMapper.updateWiki(param);
        }

        if (updateWiki > 0) {
            // tag 삭제
            int deleteWikiTag = wikiMngMapper.deleteWikiTag(param);
            // tag 넣기
            if (deleteWikiTag > 0) {
                String[] tagList = ((String)param.get("hashTag")).split(",");
                for (String tag : tagList) {
                    param.put("tag_id", tag);
                    param.put("board_id", param.get("wikiId"));
                    wikiMngMapper.insertWikiTag(param);
                }
            }
        }
        return result;
    }

    @Transactional
    public int deleteWiki(Map<String, Object> param) {
        int result = -1;

        int wikiId = ((Long) param.get("id")).intValue();
        param.put("wikiId", wikiId);

        // tag 삭제
        int deleteWikiTag = wikiMngMapper.deleteWikiTag(param);
        if (deleteWikiTag > 0) {
            // wiki 삭제
            int delWiki = wikiMngMapper.deleteWiki(param);
            if (delWiki > 0) {
                s3Service.deleteFile((Long) param.get("thumbnailNo"));
                s3Service.deleteFile((Long) param.get("titleImgNo"));
            }
        }

        return result;
    }
}
