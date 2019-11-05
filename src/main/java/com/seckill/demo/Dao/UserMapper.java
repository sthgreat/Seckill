package com.seckill.demo.Dao;

import com.seckill.demo.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "UserMapper")
public interface UserMapper {

    @Select("select * from user where user_id = #{UserId}")
    public User getById(@Param(value = "UserId") Integer id);

    @Insert("insert into user(user_name) values(#{UserName})")
    void addUser(User user);
}
