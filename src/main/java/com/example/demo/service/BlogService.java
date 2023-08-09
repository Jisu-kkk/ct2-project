package com.example.demo.service;

import com.example.demo.repo.BlogMapper;
import com.example.demo.service.Common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CommonService commonService;

    public int selectBlogListCnt(Map<String, Object> org) {
        List<Map<String, Object>> filterTag = (List<Map<String, Object>>) org.get("filterTag");

        if (filterTag.size() <= 0) {
            org.remove("filterTag");
        }

        return blogMapper.selectBlogListCnt(org);
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
        List<Map<String, Object>> blogList = blogMapper.selectBlogList(org);

        for (Map<String, Object> param : blogList) {
            param.put("tagType", "BO002");
            param.put("tagList", commonService.selectTagList(param));
        }
        return blogList;
    }

    public Map<String, Object> selectBlogDetail(Long blogId) {
        return blogMapper.selectBlogDetail(blogId);
    }
}
