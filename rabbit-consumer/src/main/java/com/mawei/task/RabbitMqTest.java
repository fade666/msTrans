package com.mawei.task;


import com.mawei.service.RabbitmqService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import java.util.UUID;


public class RabbitMqTest {

    private Jedis jedis;

    @Autowired
    public RabbitmqService rabbitmqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RedisTemplate redisTemplate;

    @Before
     public void setup() {
         //连接redis服务器，192.168.0.100:6379
         jedis = new Jedis("1.117.215.215", 6379);
         //权限认证
         jedis.auth("root");
     }

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

    @Test
    public void redisTest(){
        try {
            //jedis.set();
        }catch (Exception e){
            System.out.println(e);
        }

    }


}
