package com.seckill.demo.Service;

import com.seckill.demo.Dao.GoodsDao;
import com.seckill.demo.Dao.OrderDao;
import com.seckill.demo.domain.Goods;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.redis.RedisService;
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

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo good) {
        //减少库存，下订单，写入秒杀订单
        boolean successs = goodsService.reduceStock(good);
        //order_info miaosha_order
        if(successs){
            return orderService.createOrder(user, good);
        }else {
            setGoodsOver(good.getId());
            return null;
        }
    }

    private void setGoodsOver(Long id) {
        redisService.set("good_status:" + id , true);
    }

    public long getMiaoshaResult(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(id,goodsId);
        if(order!=null){//秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exist("good_status:" + goodsId);
    }
}
