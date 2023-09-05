package com.example.ct2.vo.admin;

import lombok.Data;

@Data
public class UserVo {

    private Long id;
    private String email;
    private String password;
    private String phone;
    private String userName;
    private String organizationCode;
}
