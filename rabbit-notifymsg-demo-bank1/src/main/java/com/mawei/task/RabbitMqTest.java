package com.mawei.task;


import com.mawei.service.RabbitmqService;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

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

}
