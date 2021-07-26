package com.mawei.impl;

import com.mawei.callback.RabbitMqCallBack;
import com.mawei.callback.RabbitMqConfirmCallback;
import com.mawei.service.RabbitmqService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author xiejianwei
 * @ClassName RabbitmqServiceImpl
 * @Description 发送mq消息
 */
@Slf4j
@Service
public class RabbitmqServiceImpl implements RabbitmqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqConfirmCallback rabbitMqConfirmCallback;

    @Autowired
    private RabbitMqCallBack rabbitMqCallBack;

    /**
     * 发送消息到mq(单个)
     *
     * @param exchange   交换机的名称
     * @param routingKey 路由key值
     * @param messages   消息的附件消息
     */
    @Override
    public void sendMessage(String exchange, String routingKey, String messages, CorrelationData correlationData) {
        /**
         * 设置回调
         */
        rabbitTemplate.setReturnCallback(rabbitMqCallBack);
        rabbitTemplate.setConfirmCallback(rabbitMqConfirmCallback);
        rabbitTemplate.convertAndSend(exchange, routingKey, messages, correlationData);

        /*查看Mq队列中的消息*/
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        // 创建连接
        Connection connection = connectionFactory.createConnection();
        // 创建通道
        Channel channel = connection.createChannel(false);
        // 设置消息交换机
        try {
            channel.exchangeDeclare(exchange, "direct", true, false, null);
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive("order_queue");
            //获取队列中的消息个数
            Integer queueCount = declareOk.getMessageCount();

            log.info("消息数量"+queueCount);
            // 关闭通道和连接
            channel.close();
            connection.close();

        } catch (IOException | TimeoutException e) {
            log.info("连接异常");
        }

    }
}
