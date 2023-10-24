package com.example.ct2.admin.intro.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IntroMngMapper {

    List<Map<String, Object>> selectIntroList(Map<String, Object> org);

    Map<String, Object> selectIntro(Map<String, Object> param);

    int updateIntroDepth(Map<String, Object> param);

    int insertIntro(Map<String, Object> param);

    int updateIntro(Map<String, Object> param);

    int deleteIntro(Map<String, Object> param);

}
