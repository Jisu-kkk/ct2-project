package com.example.ct2.admin.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProjectMngMapper {

    int selectProjectListCnt(Map<String, Object> param);

    List<Map<String, Object>> selectProjectList(Map<String, Object> param);

    Map<String, Object> selectProject(int projectId);

    List<Integer> selectProjectTagList(int projectId);

    int insertProject(Map<String, Object> param);

    int insertProjectTag(Map<String, Object> param);

    int updateProject(Map<String, Object> param);

    int deleteProjectTag(Map<String, Object> param);

    int deleteProject(Map<String, Object> param);

}
