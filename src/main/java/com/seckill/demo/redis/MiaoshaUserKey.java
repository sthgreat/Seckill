package com.seckill.demo.redis;

public class MiaoshaUserKey extends BasePrefix {
    private MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey("id");
}
