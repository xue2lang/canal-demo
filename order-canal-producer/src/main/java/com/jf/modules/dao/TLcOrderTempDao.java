package com.jf.modules.dao;

import com.jf.modules.entity.TLcOrderTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * com.nfbank.mq.consumer.dao
 * <p>
 * 订单消费者接口
 *
 * @author 孙威
 * @date 2018/9/25 17:22
 */
public interface TLcOrderTempDao {

    /**
     * 批量插入
     *
     * @return
     */
    int batchInsert(List<TLcOrderTemp> list);

    int insertSql(@Param("cols") String cols,@Param("vals") String vals);
}
