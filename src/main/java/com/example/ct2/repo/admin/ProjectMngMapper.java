package com.example.ct2.repo.admin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface ProjectMngMapper {

    int selectProjectListCnt(Map<String, Object> param);

}
