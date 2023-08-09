package com.example.demo.repo.Common;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CommonMapper {

    List<Map<String, Object>> selectTagList(Map<String, Object> param);
}
