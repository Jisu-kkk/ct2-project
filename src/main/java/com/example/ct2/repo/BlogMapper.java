package com.example.ct2.repo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BlogMapper {

    int selectBlogListCnt(Map<String, Object> org);

    List<Map<String, Object>> selectBlogList(Map<String, Object> org);

    Map<String, Object> selectBlogDetail(Long blogId);

    List<Map<String, Object>> selectBoardTag(Map<String, Object> org);
}
