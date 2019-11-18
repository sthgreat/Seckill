package com.seckill.demo.domain;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Timestamp createDate;
    private Timestamp payDate;
}
