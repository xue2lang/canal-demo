package com.jf.modules.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * com.jf.modules.listener
 * <p>
 * 顺序消费实现类
 *
 * @author 孙威
 * @date 2018/11/7 14:01
 */
@Slf4j
@Component
public class MqMsgListenerOrderlyProcessor extends MqMsgListenerAbstract implements MessageListenerOrderly {
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        //设置自动提交
        context.setAutoCommit(true);

        //对消息进行处理
        consumeMessage(msgs);

        return ConsumeOrderlyStatus.SUCCESS;
    }
}
