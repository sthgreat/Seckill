package com.seckill.demo.RabbitMQ;

import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.MiaoshaService;
import com.seckill.demo.Service.OrderService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaOrder;

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        MiaoshaMessage mm = Util.stringToBean(message, MiaoshaMessage.class);
        MiaoShaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock<=0){
            return;
        }
        //判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(),goodsId);
        if(order!=null){
            return;
        }
        //减库存，写入秒杀订单
        miaoshaOrder.miaosha(user,goods);
    }
}
