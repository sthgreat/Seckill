package com.seckill.demo.Service;

import com.seckill.demo.Dao.MiaoshaUserDao;
import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Utils.MD5Util;
import com.seckill.demo.Utils.UUIDUtil;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.exception.GlobalException;
import com.seckill.demo.redis.RedisService;
import com.seckill.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
//    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    public static final String COOKIE_NAME_TOKEN = "token";

    @Resource
    private MiaoshaUserDao miaoshaUserDao;

    @Autowired
    private RedisService redisService;

    public MiaoShaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoShaUser user = (MiaoShaUser) redisService.get("token:"+token);
        if(user != null){
            addCookie(response, token, user);
        }
        return user;
    }

    public MiaoShaUser getById(Long id){
        //取缓存
        MiaoShaUser user = (MiaoShaUser) redisService.get("userId,"+id);
        if(user!=null){
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if(user!=null){
            redisService.set("userId,"+id,user, (long) 50000);
        }
        return user;
    }

    public boolean updatePassword(String token, Long id, String formPassword){
        //取user
        MiaoShaUser miaoShaUser = getById(id);
        if(miaoShaUser==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoShaUser toBeUpdate = new MiaoShaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPassword,miaoShaUser.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //更新缓存
        redisService.delete("userId,"+id);
        miaoShaUser.setPassword(toBeUpdate.getPassword());
        redisService.set("token:"+token,miaoShaUser);
        return true;
    }

    public String login(LoginVo loginVo, HttpServletResponse response) {
        if(loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //通过手机验证用户是否存在
        MiaoShaUser user = getById(Long.parseLong(mobile));
        if(user== null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response,String token, MiaoShaUser user){
        redisService.set("token:"+token,user, (long) 300); //缓存5分钟
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(300);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
