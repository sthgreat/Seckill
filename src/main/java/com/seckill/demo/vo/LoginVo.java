package com.seckill.demo.vo;

import com.seckill.demo.Validator.isMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @isMobile
    private String mobile;

    @NotNull
    private String password;
}
