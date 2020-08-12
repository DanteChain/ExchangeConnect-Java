package com.exchange.domain;

import com.exchange.enums.OrderSide;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketTrade {
    private BigDecimal price;
    private BigDecimal num; //volume
    private long time;
    private OrderSide type;




}
