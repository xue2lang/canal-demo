package com.jf.modules.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.jf.modules.entity.TLcOrderTemp;
import com.jf.modules.service.BatchOptThreadComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * com.nfbank.mq.consumer.listener
 * <p>
 * 消费者监听，随机消费
 *
 * @author 孙威
 * @date 2018/9/18 11:37
 */
@Slf4j
@Component
public abstract class MqMsgListenerAbstract {

    /**
     * 主题
     */
    @Value("${rocketmq.msg.topic}")
    private String topicNames;
    /**
     * tag标签
     */
    @Value("${rocketmq.msg.tag}")
    private String tagNames;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.consumer.retryTimesWhenGetFailed}")
    private int retryTimesWhenGetFailed;
    /**
     * 入库dao
     */
    @Autowired
    private BatchOptThreadComponent batchOptThreadComponent;


    /**
     *  默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     *  不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    public void consumeMessage(List<MessageExt> msgs) {

        try {
            List<TLcOrderTemp> list = Lists.newArrayList();

            log.info("接受到的消息数量为：{}",msgs.size());

            for (MessageExt messageExt : msgs) {
                try {
                    if(messageExt.getTopic().equals(this.topicNames) && messageExt.getTags().equals(this.tagNames)){
                        // 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
                        // 获取该消息重试次数
                        int reconsume = messageExt.getReconsumeTimes();
                        //消息已经重试了3次，如果不需要再次消费，则放弃此消费，并打印日志
                        if(reconsume ==this.retryTimesWhenGetFailed){
                            log.info("一直未消费成功，放弃此次消费，消费的msgId为：{}",messageExt.getMsgId());
                        }else{
                            //将msg信息转换
                            byte[] body = messageExt.getBody();
                            if (body.length > 0) {
                                //添加到消费列表
                                JSONObject jsonObject = JSONObject.parseObject(body, JSONObject.class);

//                                log.info("信息为：{}",jsonObject);

                                //单条处理入库
                                insert(jsonObject);

                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("消费者处理某条消息时异常，异常信息：",e);
                }
            }

            //将消息批量入库
            if (!CollectionUtils.isEmpty(list)) {

                batchOptThreadComponent.batchInsert(list);

            }

        } catch (Exception e) {
           log.error("消费者处理批量信息时异常，异常信息：",e);
        } finally {

        }

    }



    private void insert(JSONObject jsonObject) {

        StringBuilder keySb = new StringBuilder();
        StringBuilder valSb = new StringBuilder();
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            keySb.append(",`").append(key).append("`");
            if (key.equals("create_time")) {
                valSb.append(",").append("now()");
            }else{
                valSb.append(",'").append(jsonObject.getString(key)).append("'");
            }
        }

        batchOptThreadComponent.insertSql(keySb.toString().replaceFirst(",", ""),valSb.toString().replaceFirst(",", ""));
    }
}
