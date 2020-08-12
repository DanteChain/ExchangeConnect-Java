package com.exchange.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAsset {
    private long userId;

    private String assetCode;

    private BigDecimal amountAvailable;

    private BigDecimal spotLock;

    private BigDecimal c2cLock;

    private BigDecimal futuresLock;
}
