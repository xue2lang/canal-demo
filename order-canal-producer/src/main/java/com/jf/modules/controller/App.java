package com.jf.modules.controller;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;
import java.util.List;

public class App {

    public static void main(String[] args) throws InterruptedException {

        // 第一步：与canal进行连接
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111),"example", "", "");
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("101.201.224.95", 11111),"example", "appuser", "I31LrnH7VMaW");
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("101.201.224.95", 11111),"example", "canal", "0bm_nJC0Ho_h6b");

        try {
            //外层死循环，在canal节点宕机后，抛出异常，等待zk对canal处理机的切换，切换后，继续连接处理数据
            while (true) {
                connector.connect();

                // 第二步：开启订阅
                connector.subscribe();
                System.out.println("开始订阅了---");
                // 第三步：循环订阅
                //内层死循环，按频率实时监听数据变化，一旦受到变化消息，立即消费处理，并ack，此处可考虑异步处理后再ack
                while (true) {
                    //设置默认的数据偏移量
                    long batchID = 0;
                    try {
                        // 获取指定数量的数据，此处每次读取 1000 条
                        Message message = connector.getWithoutAck(1000);

                        batchID = message.getId();

                        int size = message.getEntries().size();
                        // 偏移量不等于-1或者获取到数据不等于0时
                        if (batchID == -1 || size == 0) {
                            System.out.println("当前暂时没有数据");
                            Thread.sleep(1000);
                        } else {
                            System.out.println("-------------------------- 有数据啦 -----------------------");
                            PrintEntry(message.getEntries());
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        //处理失败，偏移量回滚
                        connector.rollback(batchID);

                    } finally {
                        // 提交确认 position id ack （方便处理下一条）
                        connector.ack(batchID);
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    /**
     * 获取每条打印的记录
     * @param entrys
     */
    @SuppressWarnings("static-access")
    public static void PrintEntry(List<Entry> entrys) {

        for (Entry entry : entrys) {

            // 第一步：拆解entry 实体
            Header header = entry.getHeader();
            EntryType entryType = entry.getEntryType();

            // 第二步： 如果当前是RowData，那就是我需要的数据
            if (entryType == EntryType.ROWDATA) {

                String tableName = header.getTableName();
                String schemaName = header.getSchemaName();

                RowChange rowChange = null;

                try {
                    rowChange = RowChange.parseFrom(entry.getStoreValue());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

                EventType eventType = rowChange.getEventType();

                System.out.println(String.format("当前正在操作 %s.%s， Action=%s", schemaName, tableName, eventType));

                // 如果是‘查询’ 或者 是 ‘DDL’ 操作，那么sql直接打出来
                if (eventType == EventType.QUERY || rowChange.getIsDdl()) {
                    System.out.println("rowchange sql ----->" + rowChange.getSql());
                    return;
                }

                // 第三步：追踪到 columns 级别
                rowChange.getRowDatasList().forEach((rowData) -> {

                    // 获取更新之前的column情况
                    List<Column> beforeColumns = rowData.getBeforeColumnsList();

                    // 获取更新之后的 column 情况
                    List<Column> afterColumns = rowData.getAfterColumnsList();

                    // 当前执行的是 删除操作
                    if (eventType == EventType.DELETE) {
                        PrintColumn(beforeColumns);
                    }

                    // 当前执行的是 插入操作
                    if (eventType == eventType.INSERT) {
                        PrintColumn(afterColumns);
                    }

                    // 当前执行的是 更新操作
                    if (eventType == eventType.UPDATE) {
                        PrintColumn(afterColumns);
                    }
                });
            }
        }
    }

    /**
     * 每个row上面的每一个column 的更改情况
     * @param columns
     */
    public static void PrintColumn(List<Column> columns) {

        columns.forEach((column) -> {

            String columnName = column.getName();
            String columnValue = column.getValue();
            String columnType = column.getMysqlType();
            // 判断 该字段是否更新
            boolean isUpdated = column.getUpdated();

            System.out.println(String.format("columnName=%s, columnValue=%s, columnType=%s, isUpdated=%s", columnName,
                    columnValue, columnType, isUpdated));

        });

    }
}