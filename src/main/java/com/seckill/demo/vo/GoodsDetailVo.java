package com.seckill.demo.vo;

import com.seckill.demo.domain.MiaoShaUser;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSec = 0;
    private GoodsVo goods;
    private MiaoShaUser miaoShaUser;
}
