package com.mawei.task;


import com.mawei.service.RabbitmqService;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class RabbitMqTest {

    @Autowired
    public RabbitmqService rabbitmqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg(){
        //rabbitmqService.sendMessage("TestDirectExchange","TestDirectRouting","测试",new CorrelationData("1"));
        rabbitTemplate.convertAndSend("TestDirectExchange","TestDirectRouting","测试",new CorrelationData("1"));
    }

    @Test
    public void uuidTest(){
        for(int i =0;i<10 ;i++){
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            System.out.println(uuid);
        }

    }

}
