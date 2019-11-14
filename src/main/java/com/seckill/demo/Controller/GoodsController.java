package com.seckill.demo.Controller;

import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping("/goods")
public class GoodsController {
//    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_list")
    public String toLogin(Model model,
                          @CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
                          @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken){
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        MiaoShaUser user = miaoshaUserService.getByToken(token);
        model.addAttribute("user",user);
        return "goods_list";
    }

}
