package com.seckill.demo.Controller;

import com.seckill.demo.Interceptor.CurrentUser;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_list")
    public String toLogin(Model model, HttpServletRequest request,
                            @CurrentUser MiaoShaUser user
//                          @CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//                          @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken
                            ){
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        MiaoShaUser user = miaoshaUserService.getByToken(response, token);
        MiaoShaUser user1 = (MiaoShaUser) request.getAttribute("current_user");
        if(user1 == null){ //未登录则返回登陆页面
            return "login.html";
        }
        model.addAttribute("user",user1);
        return "goods_list";
    }
}
