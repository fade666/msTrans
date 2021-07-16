package com.mawei.config;

import com.mawei.config.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new Queue(RabbitMqConstant.ORDER_QUEUE, true);
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
}