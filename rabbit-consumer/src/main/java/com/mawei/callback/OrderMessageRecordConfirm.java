package com.mawei.callback;

import com.mawei.entity.pojo.MessageRecord;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageRecordConfirm implements MessageRecordCallback{

    @Override
    public boolean confirm(MessageRecord messageRecord) {
        String messageId = messageRecord.getMessageId();
        /**
         * 根据具体的业务，判断是否需要提交或者回滚消息
         */
        if ("1212321".equals(messageId)) {
            return true;
        }
        return false;
    }
}
