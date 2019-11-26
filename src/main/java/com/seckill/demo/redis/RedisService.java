package com.seckill.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {//分库及其他操作
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void set(String key, Object value, Long timeOutSec) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, timeOutSec, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public Object decr(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.decrement(key);
        return valueOperations.get(key);
    }

    public Integer incr(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.increment(key);
        return (Integer) valueOperations.get(key);
    }

    public boolean exist(String key){
        return redisTemplate.hasKey(key);
    }

    public void delete(String s) {
        redisTemplate.delete(s);
    }
}
