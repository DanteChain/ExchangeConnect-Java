package com.exchange.domain;

import com.exchange.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserOrder {
    private BigDecimal filledQty;

    private BigDecimal remainingQty;

    private long orderNo;

    private InstrumentType iType;

    private OrderSide orderSide;

    private OrderType orderType;

    private String symbol;

    private BigDecimal price;

    private BigDecimal quantity;

    private String tradeCoinStatus;

    private BigDecimal disclosedQty;

    private BigDecimal disclosedQtyRemaining;

    private TimeInForce timeInForce;

    private ExecutionType executionType;

    private TriggerOn triggerOn;

    private BigDecimal triggerPrice;

    private long userId;

    private Date createTime;

    private Date updateTime;

    private long statusId;
}
