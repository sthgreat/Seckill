package com.seckill.demo.Controller;

import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.UserService;
import com.seckill.demo.domain.MiaoShaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoShaUser> info(HttpServletRequest request){
        return Result.success((MiaoShaUser) request.getAttribute("current_user"));

    }
}
