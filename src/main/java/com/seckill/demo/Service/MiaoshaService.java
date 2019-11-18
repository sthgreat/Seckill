package com.seckill.demo.Service;

import com.seckill.demo.Dao.GoodsDao;
import com.seckill.demo.Dao.OrderDao;
import com.seckill.demo.domain.Goods;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo good) {
        //减少库存，下订单，写入秒杀订单
        goodsService.reduceStock(good);
        //order_info miaosha_order
        return orderService.createOrder(user, good);
    }
}
