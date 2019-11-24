package com.seckill.demo.Controller;

import com.seckill.demo.RabbitMQ.MQSender;
import com.seckill.demo.RabbitMQ.MiaoshaMessage;
import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.MiaoshaService;
import com.seckill.demo.Service.OrderService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.redis.RedisService;
import com.seckill.demo.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaoController implements InitializingBean {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;

    @RequestMapping(value = "/do_miaosha2",method = RequestMethod.POST)
    public Result<OrderInfo> miaosha(Model model, HttpServletRequest request,
                                     @RequestParam(value = "goodsId") long goodsId){
        MiaoShaUser user = (MiaoShaUser) request.getAttribute("current_user");
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断商品是否有库存
        GoodsVo good = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = good.getGoodsStock();
        if(stock<=0){
            return Result.error(CodeMsg.Stock_Null);
        }
        //判断是否已经秒杀到，防止一个人多次秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order!=null){
            return Result.error(CodeMsg.NOMORE_MIAOSHA);
        }

        //减库存，下订单，写入秒杀订单，需要事务
        OrderInfo orderInfo = miaoshaService.miaosha(user, good);
        return Result.success(orderInfo);

    }

    //改造后
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    public Result<Integer> miaosha(HttpServletRequest request,Model model,
                                     @RequestParam(value = "goodsId") long goodsId){
        MiaoShaUser user = (MiaoShaUser) request.getAttribute("current_user");
        model.addAttribute("user",user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //预减库存
        Integer stock = (Integer) redisService.decr("goodCount,id="+goodsId);
        if(stock<0){
            return Result.error(CodeMsg.NOMORE_MIAOSHA);
        }
        //判断重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order!=null){
            return Result.error(CodeMsg.NOMORE_MIAOSHA);
        }
        //入消息队列
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        return Result.success(0); //排队中
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //系统初始化时启动该服务
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null){
            return;
        }
        for(GoodsVo goods : goodsList){
            redisService.set("goodCount,id="+goods.getId(), goods.getStockCount(), (long) 30000);
        }
    }

    //orderId：成功,-1：库存不足,0：排队中
    @RequestMapping(value = "/result", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> miaoshaResult(HttpServletRequest request,Model model,
                                         @RequestParam(value = "goodsId") long goodsId){
        MiaoShaUser user = (MiaoShaUser) request.getAttribute("current_user");
        model.addAttribute("user",user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }
}
