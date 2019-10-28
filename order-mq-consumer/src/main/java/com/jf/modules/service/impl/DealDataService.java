package com.jf.modules.service.impl;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jf.modules.service.TableOptInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * com.jf.modules.service
 * <p>
 * TODO
 *
 * @author 孙威
 * @date 2018/11/5 17:34
 */
@Slf4j
@Service
public class DealDataService {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    ConcurrentHashMap<String,TableOptInterface> tableMap = new ConcurrentHashMap();


    /**
     * 用于初始化处理数据的结构</br>
     * 基于表名获取接口TableOptInterface实现类,在配置文件中配置：表名=接口实现类全路径
     *
     */
    @PostConstruct
    public void init() {

    }

    /**
     * 处理拉取的数据
     * @param entrys
     */
    public void dealEntry(List<CanalEntry.Entry> entrys) {

        for (CanalEntry.Entry entry : entrys) {

            // 第一步：拆解entry 实体
            CanalEntry.Header header = entry.getHeader();
            EntryType entryType = entry.getEntryType();

            //消费的binlog名称及消费位点
            String logFileName = header.getLogfileName();
            long logFileOffset = header.getLogfileOffset();
            log.info(String.format("binlog名称：%s ，消费位点：%s", logFileName,String.valueOf(logFileOffset)));

            // 第二步： 如果当前是RowData，那就是我需要的数据
            if (entryType == EntryType.ROWDATA) {

                RowChange rowChange = null;

                try {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                } catch (InvalidProtocolBufferException e) {
                    log.error("处理每一行的数据时异常，异常信息：",e);
                    continue;
                }
                //此处可将需要同步的表名存储到集合中（前提canal订阅全部表结构）
                String schemaName = header.getSchemaName();
                String tableName = header.getTableName();

                //事件类型
                CanalEntry.EventType eventType = rowChange.getEventType();

                log.info(String.format("当前正在操作 %s.%s， Action=%s", schemaName, tableName, eventType));

                // 如果是‘查询’ 或者 是 ‘DDL’ 操作，那么sql直接打出来
                if (EventType.QUERY.equals(eventType) || rowChange.getIsDdl()) {
                    log.info("当前行的操作为查询或DDL ：{}" , rowChange.getSql());
                    continue;
                }

                // 第三步：追踪到 columns 级别
                List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();

                //此处处理是否开异步，需要根据具体的业务来定义
                for(CanalEntry.RowData rowData : rowDatasList){

                    dealRowDataByEventType(rowData, eventType,tableName);

                }
            }else {
                log.info("数据库配置非基于行模式");
            }
        }
    }

    /**
     * 按照事件类型处理每一行数据
     * @param rowData
     * @param eventType
     * @param tableName
     */
    public void dealRowDataByEventType(CanalEntry.RowData rowData, CanalEntry.EventType eventType,String tableName) {
        switch (eventType) {
            // 当前执行的是 删除操作
            case DELETE:
                dealDeleteRow(rowData.getBeforeColumnsList(),tableName);
                break;
            // 当前执行的是 插入操作
            case INSERT:
                dealInsertRow(rowData.getAfterColumnsList(),tableName);
                break;
            // 当前执行的是 更新操作
            case UPDATE:
                dealUpdateRow(rowData.getAfterColumnsList(),tableName);
                break;
            default:
                    break;
        }

    }

    private void dealUpdateRow(List<Column> afterColumns,String tableName) {
        //根据不同的表名获取不同的业务实现
    }

    private void dealInsertRow(List<Column> afterColumns,String tableName) {
    }

    private void dealDeleteRow(List<Column> beforeColumns,String tableName) {
    }

}
