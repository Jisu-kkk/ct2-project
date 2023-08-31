package com.example.ct2.repo.admin;

import com.example.ct2.vo.admin.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserVo selectUserByEmail(String email);

    UserVo selectUserById(Long id);
}
