package com.seckill.demo.RabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";

    @Bean
    public Queue queue(){
        return new Queue(MQConfig.QUEUE,true); // 队列名称，是否持久化
    }

}
