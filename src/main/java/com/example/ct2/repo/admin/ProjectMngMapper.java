package com.example.ct2.repo.admin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProjectMngMapper {

    int selectProjectListCnt(Map<String, Object> param);

    List<Map<String, Object>> selectProjectList(Map<String, Object> param);

    int insertProject(Map<String, Object> param);

    int insertProjectTag(Map<String, Object> param);

    Map<String, Object> selectProject(int projectId);

    List<Integer> selectProjectTagList(int projectId);

}
