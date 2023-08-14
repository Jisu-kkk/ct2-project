package com.example.ct2.repo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IntroMapper {

    List<Map<String, Object>> selectIntroList(Map<String, Object> org);
}
