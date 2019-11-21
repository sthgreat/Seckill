package com.seckill.demo.Service;

import com.seckill.demo.Dao.GoodsDao;
import com.seckill.demo.Dao.OrderDao;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.redis.RedisService;
import com.seckill.demo.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(Long id, long goodsId) {
//        return orderDao.getMiaoshaOrderByUserIdAndGoodsId(id, goodsId);
        return (MiaoshaOrder) redisService.get("MiaoShaOrder:uid="+id+",gid="+goodsId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo good) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(good.getId());
        orderInfo.setGoodsName(good.getGoodsName());
        orderInfo.setGoodsPrice(good.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        Long orderId = orderDao.addOrder(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(good.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        redisService.set("MiaoShaOrder:uid="+user.getId()+",gid="+good.getId(),miaoshaOrder);
        orderDao.addMiaoshaOrder(miaoshaOrder);

        return orderInfo;
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }
}
