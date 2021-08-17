package com.mawei.listener;

import com.mawei.config.constant.RabbitMqConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import java.io.IOException;

@Component
public class DeadLetterListener {

    @RabbitListener(queues = RabbitMqConstant.DEAD_LETTER_QUEUE)
    public void receiveA(Message message, Channel channel) throws IOException {
        System.out.println("收到死信消息" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
