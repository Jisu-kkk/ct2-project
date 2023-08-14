package com.example.ct2.repo.Common;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommonMapper {

    List<Map<String, Object>> selectTagList(Map<String, Object> param);
}
