package com.exchange.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MarketStat {
    private BigDecimal _24Price;
    private BigDecimal _24Low;
    private BigDecimal _24High;
    private BigDecimal _24Volume;
    private BigDecimal currentPrice;
}
