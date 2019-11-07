package com.seckill.demo.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class MiaoShaUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
