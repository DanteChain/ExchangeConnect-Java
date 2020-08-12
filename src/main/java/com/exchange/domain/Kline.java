package com.exchange.domain;

import java.math.BigDecimal;

public class Kline {
    private String symbol;
    private String klineType;
    private long time;
    private BigDecimal amount;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal maxPrice;
    private BigDecimal lowPrice;
}
