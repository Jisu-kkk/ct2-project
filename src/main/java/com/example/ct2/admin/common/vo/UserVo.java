package com.example.ct2.admin.common.vo;

import lombok.Data;

@Data
public class UserVo {

    private Long id;
    private String email;
    private String password;
    private String phone;
    private String userName;
    private String organizationCode;
    private String orgName;
    private Long role_id;
    private String alias;
}
