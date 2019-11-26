package com.seckill.demo.Access;

import com.seckill.demo.domain.MiaoShaUser;

public class UserContext {
    private static ThreadLocal<MiaoShaUser> userHolder = new ThreadLocal<>();

    public static void setUser(MiaoShaUser user){
        userHolder.set(user);
    }

    public static MiaoShaUser getUser(){
        return userHolder.get();
    }
}
