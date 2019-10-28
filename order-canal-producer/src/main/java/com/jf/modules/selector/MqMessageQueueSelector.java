package com.jf.modules.selector;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * com.jf.modules.selector
 * <p>
 *  发送消息到指定的队列（顺序发送）：
 * 消息队列选择器，用于发送消息时，指定所要发往的队列，以保证同一个订单会一直发送到某个队列。
 *
 * @author 孙威
 * @date 2018/11/7 11:08
 */
@Component
public class MqMessageQueueSelector implements MessageQueueSelector {
    /**
     *
     * @param mqs 消息队列
     * @param msg
     * @param arg 用于对队列取模的入参
     * @return
     */
    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        //本次arg是订单号，取hash即可
        int index;
        final int mqSize = mqs.size();
        if (arg == null) {
            //随机取值
            Random random = new Random(mqSize);
            index = random.nextInt(mqSize);
        }else{
            int hashCode = arg.toString().hashCode();
            index = Math.abs(hashCode) % mqSize;
        }
        return mqs.get(index);
    }
}
