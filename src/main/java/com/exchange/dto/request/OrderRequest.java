package com.exchange.dto.request;

import com.exchange.enums.*;

import java.math.BigDecimal;


public class OrderRequest {
    private long orderNo;

    private String symbol;

    private OrderType orderType;

    private OrderSide orderSide;

    private TimeInForce timeInForce;

    private ExecutionType executionType;

    private TriggerOn triggerOn;

    private BigDecimal disclosedQuantity;

    private BigDecimal price;

    private BigDecimal quantity;

    private BigDecimal triggerPrice;

    public long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(long orderNo) {
        this.orderNo = orderNo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderSide getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(OrderSide orderSide) {
        this.orderSide = orderSide;
    }

    public TimeInForce getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(TimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public TriggerOn getTriggerOn() {
        return triggerOn;
    }

    public void setTriggerOn(TriggerOn triggerOn) {
        this.triggerOn = triggerOn;
    }

    public BigDecimal getDisclosedQuantity() {
        return disclosedQuantity;
    }

    public void setDisclosedQuantity(BigDecimal disclosedQuantity) {
        this.disclosedQuantity = disclosedQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(BigDecimal triggerPrice) {
        this.triggerPrice = triggerPrice;
    }
}
