package com.seckill.demo.Interceptor;

import com.alibaba.fastjson.JSON;
import com.seckill.demo.Access.AccessLimit;
import com.seckill.demo.Access.UserContext;
import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            MiaoShaUser user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(user == null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key+="_"+user.getId();
            } //else : do nothing


            Integer count = (Integer) redisService.get("Access_count,"+key);
            if(count == null){
                redisService.set("Access_count,"+key, 1 ,(long) seconds);
            }else if(count < maxCount){
                redisService.incr("Access_count,"+key);
            }else{
                render(response, CodeMsg.ACCESS_LIMIT_REACH);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String cookieToken = null;
        String paramToken = null;
        if(cookies == null){
            request.setAttribute(MiaoshaUserService.COOKIE_NAME_TOKEN,null);
            return null;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(MiaoshaUserService.COOKIE_NAME_TOKEN)){
                cookieToken = cookie.getValue();
            }
        }
        if(cookieToken == null){ //如果cookie中没有token，试图从请求参数中获取
            paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        }

        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            request.setAttribute("current_user", null);
            return null;
        }else {
            String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
            MiaoShaUser user = miaoshaUserService.getByToken(response, token);
            request.setAttribute("current_user", user);
            return user;
        }
    }
}
