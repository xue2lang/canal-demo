package com.jf.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.jf.common.constant.KeysConstant;
import com.jf.modules.dao.TLcOrderTempDao;
import com.jf.modules.entity.TLcOrderTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * com.nfbank.mq.consumer.component
 * <p>
 * 批量操作
 *
 * @author 孙威
 * @date 2018/9/25 18:22
 */
@Slf4j
@Component
public class BatchOptThreadComponent {

    /**
     * 入库dao
     */
    @Autowired
    private TLcOrderTempDao orderTempDao;


    /**
     * 批量插入
     * @param list
     */
    @Async(KeysConstant.ASYNC_EXECUTOR)
    public void batchInsert(List<TLcOrderTemp> list) {

        try {
            orderTempDao.batchInsert(list);
        } finally {
            log.info("批量提交入库的信息如下：{}",JSONObject.toJSONString(list));
            list = null;
        }

    }
    @Async(KeysConstant.ASYNC_EXECUTOR)
    public void insertSql(String cols,String vals) {
        orderTempDao.insertSql(cols,vals);
    }
}
