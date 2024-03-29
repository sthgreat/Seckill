package com.seckill.demo.vo;

import com.seckill.demo.domain.Goods;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class GoodsVo {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
    private Double miaoshaPrice;
    private Integer stockCount;
    private Timestamp startDate;
    private Timestamp endDate;
}
