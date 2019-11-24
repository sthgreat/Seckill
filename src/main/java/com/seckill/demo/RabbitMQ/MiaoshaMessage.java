package com.seckill.demo.RabbitMQ;

import com.seckill.demo.domain.MiaoShaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {
    private MiaoShaUser user;
    private long goodsId;
}
