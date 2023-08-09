package com.example.demo.repo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProjectMapper {

    List<Map<String, Object>> selectProjectList(Map<String, Object> org);
}