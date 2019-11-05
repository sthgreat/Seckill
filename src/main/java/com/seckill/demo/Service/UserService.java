package com.seckill.demo.Service;

import com.seckill.demo.Dao.UserMapper;
import com.seckill.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getById(Integer id){
        return userMapper.getById(id);
    }
}
