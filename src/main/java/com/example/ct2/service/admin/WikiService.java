package com.example.ct2.service.admin;

import com.example.ct2.repo.admin.WikiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WikiService {

    @Autowired
    private WikiMapper wikiMapper;

    public int selectWikiListCnt(Map<String, Object> param) {
        return wikiMapper.selectWikiListCnt(param);
    }

    public List<Map<String, Object>> selectWikiList(Map<String, Object> param) {
        List<Map<String, Object>> selectWikiList = wikiMapper.selectWikiList(param);
        return selectWikiList;
    }
}
