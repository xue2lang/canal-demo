package com.jf.modules.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * com.nfbank.mq.consumer.model
 * <p>
 * 收益实体类
 *
 * @author 孙威
 * @date 2018/9/25 17:20
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TLcOrderTemp {

    private String orderNo;

    private String channelId;

    private String upOrderNo;

    private String tradeNo;
    private String payType;
    private String productType;

    private String productId;
    private String memberId;
    private String memberName;
    private String mobile;
    private String cardType;

    private String cardNo;
    private String accountMobile;
    private String isHorse;
    private String isFundFlow;
    private BigDecimal amount;
    private BigDecimal payAmount;
    private BigDecimal zpAmount;
    private BigDecimal discountAmount;
    private String discountWay;
    private String discountHandleWay;
    private Integer buyDeadline;
    private String deadlineUnit;
    private BigDecimal profit;
    private BigDecimal extraEarnings;
    private String extraRem;
    private BigDecimal notextraEarnings;
    private String notextraRem;
    private String orderStatus;
    private Date createTime;
    private Date arriveTime;
    private Date startTime;
    private Date endTime;
    private String zpSyncStatus;
    private String orderContinueType;
    private String inviteCode;
    private String cartId;
    private String splitState;
    private String isArrival;
    private BigDecimal advanceProfit;
    private BigDecimal surplusAmount;
    private String accountMobileStatus;
    private String isContinue;
    private String contractUrl;
    private String remark;
    private BigDecimal earnedMoney;
    private BigDecimal restAmount;
    private BigDecimal realRedeemAmount;
    private BigDecimal matchAmount;
    private String uploadStatus;
    private String accountName;
    private String isScan;
    private String matchState;
    private Integer shortPeriod;
    private String specialOrderNote;
    private String userId;
    private String antiMoneyUrl;
    private String digitalContractUrl;


}
