package com.exchange.dto.request;

import java.math.BigDecimal;

public class ModifyLeverageDto {
    private String symbol;
    private BigDecimal leverage;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getLeverage() {
        return leverage;
    }

    public void setLeverage(BigDecimal leverage) {
        this.leverage = leverage;
    }
}
