package com.jf.common.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.Message;
import com.jf.modules.service.impl.DealDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * com.jf.common.config
 * <p>
 * TODO
 *
 * @author 孙威
 * @date 2018/11/6 14:08
 */
@Slf4j
@Configuration
public class CanalClusterConfig {


    @Value("${canal.zkServers}")
    String zkServers;

    @Value("${canal.destination}")
    String destination;

    @Value("${canal.batchSize}")
    int batchSize;

    @Autowired
    private DealDataService dealDataService;

    @PostConstruct
    public void init() {
        // 第一步：配置相关信息
        CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination, StringUtils.EMPTY, StringUtils.EMPTY);
       new Thread(() -> {
           pullData(connector);
       }).start();
        log.info("异步处理canal服务-----");
    }

    private void pullData(CanalConnector connector) {

        try {

            //外层死循环，在canal节点宕机后，抛出异常，等待zk对canal处理机的切换，切换后，继续连接处理数据
            while (true) {

                //重试次数、重试，此处使用默认

                connector.connect();

                // 第二步：开启订阅
                connector.subscribe();

                //若切换zk，此处可以保证重新消费
                connector.rollback();

                log.info("启动成功了，开始订阅消息");
                long startTime = System.currentTimeMillis();

                // 第三步：循环订阅

                //内层死循环，按频率实时监听数据变化，一旦收到变化消息，立即消费处理，并ack，此处可考虑异步处理后再ack
                while (true) {

                    //设置默认的数据偏移量
                    long batchID = 0;
                    try {
                        // 获取指定数量的数据，此处每次读取 1000 条
                        Message message = connector.getWithoutAck(batchSize);

                        batchID = message.getId();

                        int size = message.getEntries().size();
                        // 偏移量不等于-1或者获取到数据不等于0时
                        if (batchID == -1 || size == 0) {
                            log.info("当前暂无数据需要同步");

                            //此处可休眠，也可不休眠，若无数据，最好休眠挂起一下
                            Thread.sleep(1000);
                        } else {
                            dealDataService.dealEntry(message.getEntries());
                        }


                    } catch (Exception e) {
                        //处理失败，偏移量回滚
                        connector.rollback(batchID);

                        log.error("处理同步数据异常，异常信息：",e);

                    } finally {

                        // 提交确认 position id ack （方便处理下一条,否则会重复消费）
                        connector.ack(batchID);

                    }
                    long endTime = System.currentTimeMillis();
                    log.info(String.format("当前已用时：%s秒",String.valueOf((endTime-startTime)/1000)));
                }
            }

        } catch (Exception e) {
            log.error("开启canal连接异常：",e);
        }
    }




    /**
     * 每个row上面的每一个column 的更改情况
     * @param columns
     */
    private void printColumn(List<Column> columns) {

        for(Column column : columns){

            String columnName = column.getName();
            String columnValue = column.getValue();
            String columnType = column.getMysqlType();
            // 判断 该字段是否更新：若是insert则所有字段都是更新，若是update则部分字段为更新
            boolean isUpdated = column.getUpdated();

            log.info(String.format("columnName=%s, columnValue=%s, columnType=%s, isUpdated=%s", columnName,columnValue, columnType, isUpdated));

        }

    }
}
