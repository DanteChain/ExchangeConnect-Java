package com.exchange.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPosition {
    private long uid;
    private String symbol;
    private BigDecimal leverage;
    private BigDecimal orderLock;

    private BigDecimal posQty;
    private BigDecimal posLock;
    private BigDecimal posPrice;
    private BigDecimal posLiqPrice;

    private BigDecimal buyOrderQty;
    private BigDecimal buyOrderValue;
    private BigDecimal buyAveragePrice;

    private BigDecimal sellOrderQty;
    private BigDecimal sellOrderValue;
    private BigDecimal sellAveragePrice;
}

