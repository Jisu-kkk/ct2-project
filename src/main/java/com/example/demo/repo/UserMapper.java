package com.example.demo.repo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    ArrayList<Map<String, Object>> selectList();
}
