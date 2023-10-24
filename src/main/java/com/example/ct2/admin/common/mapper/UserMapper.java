package com.example.ct2.admin.common.mapper;

import com.example.ct2.admin.common.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserVo selectUserByEmail(String email);

    UserVo selectUserById(Long id);
}
