package com.seckill.demo.Controller;

import com.seckill.demo.Interceptor.CurrentUser;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

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
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, HttpServletRequest request,
                         @PathVariable("goodsId") long goodsId){
        MiaoShaUser user1 = (MiaoShaUser) request.getAttribute("current_user");
        if(user1 == null){ //未登录则返回登陆页面
            return "login.html";
        }
        model.addAttribute("user",user1);
        //拿到商品的信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSec = 0;

        if(now<startAt){//秒杀未开始
            miaoshaStatus = 0;
            remainSec = (int)(startAt - now)/1000;
        }else if(now>endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSec = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSec = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSec);
        return "goods_detail";
    }
}
