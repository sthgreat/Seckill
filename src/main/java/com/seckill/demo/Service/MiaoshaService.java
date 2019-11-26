package com.seckill.demo.Service;

import com.seckill.demo.Dao.GoodsDao;
import com.seckill.demo.Dao.OrderDao;
import com.seckill.demo.Utils.MD5Util;
import com.seckill.demo.Utils.UUIDUtil;
import com.seckill.demo.domain.Goods;
import com.seckill.demo.domain.MiaoShaUser;
import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.redis.RedisService;
import com.seckill.demo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

    public boolean checkPath(MiaoShaUser user, long goodsId, String path) {
        if(path == null){
            return false;
        }
        String pathOld = (String) redisService.get("miaosha_path,"+user.getId()+"_"+goodsId);
        return path.equals(pathOld);
    }

    public String createMiaoshaPath(MiaoShaUser user,long goodsId) {
        if(user == null || goodsId <= 0){
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set("miaosha_path,"+user.getId()+"_"+goodsId, str, (long) 60);
        return str;
    }

    public BufferedImage createVerifyCode(MiaoShaUser user,long goodsId) {
        if(user == null || goodsId <= 0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        Integer rnd = calc(verifyCode);
        redisService.set("VerifyCode,"+user.getId()+","+goodsId, rnd,(long) 60);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoShaUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId<=0){
            return false;
        }
        Integer codeOld = (Integer) redisService.get("VerifyCode,"+user.getId()+","+goodsId);
        if(codeOld == null || codeOld - verifyCode != 0){
            return false;
        }
        redisService.delete("VerifyCode,"+user.getId()+","+goodsId);
        return true;
    }
}
