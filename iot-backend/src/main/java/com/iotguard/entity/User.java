package com.iotguard.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
