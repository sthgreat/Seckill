package com.seckill.demo.Controller;

import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.UserService;
import com.seckill.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    private UserService userService;

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello!");
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","jsb");
        return "hello";
    }
}
