package com.mawei.config.constant;

public class RabbitMqConstant {

    /**
     * 订单-交换机
     */
    public static final String ORDER_EXCHANGE = "order_exchange";

    /**
     * 订单-路由键
     */
    public static final String ORDER_ROUTING_KEY = "order_key";

    /**
     * 订单对列
     */
    public static final String ORDER_QUEUE = "order_queue";

    /**
     * 死信交换机
     */
    public static final String DEAD_LETTER_EXCHANGE= "dead_letter_exchange";

    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE= "dead_letter_queue";

    /**
     * 死信路由键
     */
    public static final String DEAD_LETTER_ROUTING_KEY= "dead_letter_key";

}
