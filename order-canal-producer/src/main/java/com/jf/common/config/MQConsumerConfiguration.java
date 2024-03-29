package com.jf.common.config;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.jf.modules.listener.MqMsgListenerConcurrentlyProcessor;
import com.jf.modules.listener.MqMsgListenerOrderlyProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * com.nfbank.mq.consumer.config
 * <p>
 * mq消费者实体类
 *
 * @author 孙威
 * @date 2018/9/18 11:35
 */
@Slf4j
@Configuration
public class MQConsumerConfiguration {


    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    /**
     * 消费监听，随机消费
     */
//    @Autowired
//    private MqMsgListenerConcurrentlyProcessor mqMessageListenerProcessor;
    /**
     * 消息监听，顺序消费
     */
    @Autowired
    private MqMsgListenerOrderlyProcessor mqMessageListenerProcessor;

    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer()  {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("lcht_canal_group_name");
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mqMessageListenerProcessor);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费  默认值
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //注意新主题从来没有消费过,producer如果先启动,consumer后启动，间隔时间内producer发出的消息默认是接不到的,需要如下设置
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        consumer.setMessageModel(MessageModel.CLUSTERING);

        //设置一次消费消息的条数，默认为1条
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0],topicTag[1]);
            }

            consumer.start();

            log.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr);
        }catch (MQClientException e){
            log.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}",groupName,topics,namesrvAddr,e);

        }
        return consumer;
    }


}
