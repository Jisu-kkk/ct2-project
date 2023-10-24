package com.example.ct2.main.wiki.service;

import com.example.ct2.main.wiki.mapper.WikiMapper;
import com.example.ct2.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WikiService {

    @Autowired
    private WikiMapper wikiMapper;

    @Autowired
    private CommonService commonService;

    public int selectBlogListCnt(Map<String, Object> org) {
        List<Map<String, Object>> filterTag = (List<Map<String, Object>>) org.get("filterTag");

        if (filterTag.size() <= 0) {
            org.remove("filterTag");
        }

        return wikiMapper.selectBlogListCnt(org);
    }

    public int selectBlogListEndPage(Map<String, Object> org) {
        int totalCnt = selectBlogListCnt(org);
        int postCnt = 10;
        int endPage = 0;
        if (totalCnt % postCnt == 0) {
            endPage = totalCnt / postCnt;
        } else {
            endPage = (totalCnt / postCnt) + 1;
        }
        return endPage;
    }

    public List<Map<String, Object>> selectBlogList(Map<String, Object> org) {
        List<Map<String, Object>> blogList = wikiMapper.selectBlogList(org);

        for (Map<String, Object> param : blogList) {
            param.put("tagList", wikiMapper.selectBoardTag(param));
        }
        return blogList;
    }

    public Map<String, Object> selectBlogDetail(Long blogId) {
        return wikiMapper.selectBlogDetail(blogId);
    }
}
