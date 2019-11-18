package com.seckill.demo.Service;

import com.seckill.demo.Dao.GoodsDao;
import com.seckill.demo.domain.Goods;
import com.seckill.demo.domain.MiaoshaGoods;
import com.seckill.demo.vo.GoodsVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo good) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(good.getId());
        goodsDao.reduceStock(g);
    }
}
