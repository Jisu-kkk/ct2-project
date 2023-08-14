package com.example.ct2.repo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface OrganizationMapper {

    Map<String, Object> selectOrg(String type);
}
