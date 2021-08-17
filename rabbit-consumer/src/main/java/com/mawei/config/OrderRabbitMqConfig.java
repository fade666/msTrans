package com.mawei.config;

import com.mawei.config.constant.RabbitMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author xiejianwei
 * @ClassName BusinessOrderRabbitMqConfig
 */
@Configuration
public class OrderRabbitMqConfig {

    /**
     * 初始化队列
     *
     * @return
     */
    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", RabbitMqConstant.DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", RabbitMqConstant.DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(RabbitMqConstant.ORDER_QUEUE).withArguments(args).build();

    }

    /**
     * 初始化交换机
     *
     * @return
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(RabbitMqConstant.ORDER_EXCHANGE, true, false);
    }

    /**
     * 队列通过路由键绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding bind() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(RabbitMqConstant.ORDER_ROUTING_KEY);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(RabbitMqConstant.DEAD_LETTER_EXCHANGE);
    }

    // 声明死信队列A
    @Bean("deadLetterQueue")
    public Queue deadLetterQueueA(){
        return new Queue(RabbitMqConstant.DEAD_LETTER_QUEUE);
    }

    // 声明死信队列A绑定关系
    @Bean
    public Binding deadLetterBindingA(@Qualifier("deadLetterQueue") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMqConstant.DEAD_LETTER_ROUTING_KEY);
    }
}