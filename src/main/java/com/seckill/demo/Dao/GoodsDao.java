package com.seckill.demo.Dao;

import com.seckill.demo.domain.Goods;
import com.seckill.demo.domain.MiaoshaGoods;
import com.seckill.demo.domain.OrderInfo;
import com.seckill.demo.vo.GoodsVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "GoodsDao")
public interface GoodsDao {
    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.id = g.id")
//    @Results(id = "GoodsVoMap",value = {
//            @Result(column = "id",property = "id", id = true),
//            @Result(column = "goods_name", property = "goodsName"),
//            @Result(column = "goods_title",property = "goodsTitle"),
//            @Result(column = "goods_img",property = "goodsImg"),
//            @Result(column = "goods_detail",property = "goodsDetail"),
//            @Result(column = "goods_price",property = "goodsPrice"),
//            @Result(column = "goods_stock",property = "goodsStock"),
//            @Result(column = "stock_count", property = "stockCount"),
//            @Result(column = "start_date",property = "startDate"),
//            @Result(column = "end_date",property = "endDate")
//    })
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.id = g.id where g.id = #{id}")
    GoodsVo getGoodsVoByGoodsId(@Param(value = "id") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(MiaoshaGoods g);
}
