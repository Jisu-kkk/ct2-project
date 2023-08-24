package com.example.ct2.repo.admin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface WikiMapper {

    int selectWikiListCnt(Map<String, Object> param);

    List<Map<String, Object>> selectWikiList(Map<String, Object> param);

    int insertWiki(Map<String, Object> param);

    int insertWikiTag(Map<String, Object> param);

    Map<String, Object> selectWiki(int wikiId);

    List<Integer> selectWikiTagList(int wikiId);

    int updateWiki(Map<String, Object> param);

    int deleteWikiTag(Map<String, Object> param);

    int deleteWiki(Map<String, Object> param);
}
