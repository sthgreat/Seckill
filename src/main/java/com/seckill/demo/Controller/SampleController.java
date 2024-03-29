package com.seckill.demo.Controller;

import com.seckill.demo.RabbitMQ.MQSender;
import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.UserService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.User;
import com.seckill.demo.redis.RedisUtil;
import com.seckill.demo.vo.GoodsVo;
import org.apache.ibatis.annotations.Arg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    private MQSender mqSender;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoodsService goodsService;

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

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<User> set(){
        User user = new User();
        user.setUserName("jsb");
        user.setUserId(2);
        redisTemplate.opsForValue().set("345",user);
        User result = (User) redisTemplate.opsForValue().get("345");
        return Result.success(result);
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello!man!");
        return Result.success("H");
    }


    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> get(){
        return Result.success((User)redisUtil.get("2"));
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        GoodsVo g = goodsService.getGoodsVoByGoodsId(1);

        return g.toString();
    }
}
