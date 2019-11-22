package com.seckill.demo.RabbitMQ;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(Object message){
        String msg = Util.beanToString(message);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

}
