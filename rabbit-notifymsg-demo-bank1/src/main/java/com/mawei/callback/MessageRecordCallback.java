package com.mawei.callback;

import com.mawei.entity.pojo.MessageRecord;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 消息回调，有具体的生产者处理
 *
 * @author xiejianwei
 */
@Component
public interface MessageRecordCallback {

    /**
     * 回调确认是否需要提交消息
     * false则删除消息，true提交消息到MQ
     *
     * @param messageRecord
     * @return
     */
    boolean confirm(MessageRecord messageRecord);

}