package com.seckill.demo.Controller;

import com.seckill.demo.Result.CodeMsg;
import com.seckill.demo.Result.Result;
import com.seckill.demo.Service.GoodsService;
import com.seckill.demo.Service.MiaoshaService;
import com.seckill.demo.Service.OrderService;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaoController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
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
}
