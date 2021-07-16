package com.mawei.callback;

import com.alibaba.fastjson.JSON;
import com.mawei.config.constant.RabbitMqConstant;
import com.mawei.entity.pojo.MessageRecord;
import com.mawei.service.MessageRecordService;
import com.mawei.service.RabbitmqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiejianwei
 */
@Component
public class RabbitMqConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfirmCallback.class);

    @Autowired
    private MessageRecordService messageRecordService;

    @Autowired
    public RabbitmqService rabbitmqService;

    /**
     * @param correlationData 相关配置信息
     * @param ack             交换机是否成功收到消息
     * @param cause           错误信息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        /**
         * 这个就是我们发送消息设置的messageId
         */
        logger.info("发送成功消息,调用回调方法");
        String messageId = correlationData.getId();

        logger.info("是否成功"+String.valueOf(ack));
        // 未发送成功
        if (!ack) {
            logger.info("发送消息失败了");
            MessageRecord messageRecord = messageRecordService.find(messageId);
            if (null != messageRecord) {
                // 重发
                rabbitmqService.sendMessage(RabbitMqConstant.ORDER_EXCHANGE, RabbitMqConstant.ORDER_ROUTING_KEY, JSON.toJSONString(messageRecord), new CorrelationData(messageRecord.getMessageId()));
            }
        } else {

            // 修改消息状态为成功
            logger.info("发送成功了！");
            messageRecordService.update(messageId);
        }
    }

}
