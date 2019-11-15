package com.seckill.demo.Interceptor;

import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.domain.MiaoShaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String cookieToken = null;
        String paramToken = null;
        if(cookies == null){
            request.setAttribute(MiaoshaUserService.COOKIE_NAME_TOKEN,null);
            return true;
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
            return true;
        }else {
            String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
            MiaoShaUser user = miaoshaUserService.getByToken(response, token);
            request.setAttribute("current_user", user);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
