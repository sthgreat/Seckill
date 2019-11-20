package com.seckill.demo.Dao;

import com.seckill.demo.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "MiaoshaUserDao")
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoShaUser getById(@Param(value = "id") Long id);

    @Update("update miaosha_user set password=#{password} where id=#{id}")
    void update(MiaoShaUser toBeUpdate);
}
