package com.seckill.demo.RabbitMQ;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";
    public static final String MIAOSHA_QUEUE = "miaoshaQueue";

    @Bean
    public Queue queue(){
        return new Queue(MQConfig.QUEUE,true); // 队列名称，是否持久化
    }

    @Bean
    public Queue miaoshaQ(){
        return new Queue(MQConfig.MIAOSHA_QUEUE,true);
    }

}
