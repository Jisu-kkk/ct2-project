package com.example.ct2.admin.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FileMapper {

    int insertFile(Map<String, Object> param);

    Map<String, Object> selectFile(Map<String, Object> param);

    int updateFile(Map<String, Object> param);

    int deleteFile(Map<String, Object> param);
}
