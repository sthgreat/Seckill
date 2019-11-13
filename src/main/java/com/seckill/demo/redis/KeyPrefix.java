package com.seckill.demo.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
