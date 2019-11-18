package com.seckill.demo.Dao;

import com.seckill.demo.domain.MiaoshaOrder;
import com.seckill.demo.domain.OrderInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "OrderDao")
public interface OrderDao {

    @Select("select * from miaosha_order where user_id=#{userId} and goods_id = #{goodId}")
    MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(@Param("userId") Long id, @Param("goodId") long goodsId);

    @Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date)" +
            " values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    Long addOrder(OrderInfo orderInfo);

    @Insert("insert into miaosha_order(user_id,goods_id,order_id) values(#{userId},#{goodsId},#{orderId})")
    int addMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
