package com.seckill.demo.Controller;

import com.seckill.demo.Dao.MiaoshaUserDao;
import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.MiaoshaUserService;
import com.seckill.demo.Utils.ValidatorUtil;
import com.seckill.demo.vo.LoginVo;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
//    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(@Valid LoginVo loginVo, HttpServletResponse response){
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        if(StringUtils.isEmpty(passInput)){
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if(StringUtils.isEmpty(mobile)){
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        String token = miaoshaUserService.login(loginVo, response);
        return Result.success(token);
    }
}
