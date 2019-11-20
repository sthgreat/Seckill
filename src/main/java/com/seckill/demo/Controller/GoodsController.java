package com.seckill.demo.Controller;

import com.seckill.demo.Interceptor.CurrentUser;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.redis.RedisService;
import com.seckill.demo.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toLogin(Model model, HttpServletRequest request, HttpServletResponse response
                            ){
        MiaoShaUser user1 = (MiaoShaUser) request.getAttribute("current_user");
        if(user1 == null){ //未登录则返回登陆页面
            return "login.html";
        }
        model.addAttribute("user",user1);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        //取缓存中的html
        String html = (String) redisService.get("goods_list");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",webContext);
        if(!StringUtils.isEmpty(html)){
            redisService.set("goods_list",html, (long) 60);
        }

        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail(Model model, HttpServletRequest request,HttpServletResponse response,
                         @PathVariable("goodsId") long goodsId){
        MiaoShaUser user1 = (MiaoShaUser) request.getAttribute("current_user");
        if(user1 == null){ //未登录则返回登陆页面
            return "login.html";
        }
        model.addAttribute("user",user1);

        String html = (String) redisService.get("goods_detail,id="+goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //拿到商品的信息
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        log.info(startAt+" "+endAt+" "+ now);

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
//        return "goods_detail";

        //手动渲染
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",webContext);
        if(!StringUtils.isEmpty(html)){
            redisService.set("goods_detail,id=" + goodsId,html, (long) 5);
        }
        return html;
    }
}
