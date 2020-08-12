package com.exchange.enums;

import static com.exchange.enums.ApiType.PRIVATE;
import static com.exchange.enums.ApiType.PUBLIC;

public enum RequestType {

    TRADE_HISTORY("market/history/trade", PUBLIC),
    TICKERS("market/tickers", PUBLIC),
    DEPTH("market/depth", PUBLIC),
    KLINE("dataload/kline-query/pages", PUBLIC),
    NEW_ORDER("exchangeApi/api/v1/order-mgmt/order", PRIVATE),
    MODIFY_ORDER("exchangeApi/api/v1/order-mgmt/modify-order", PRIVATE),
    CANCEL_ORDER("exchangeApi/api/v1/order-mgmt/cancel-order", PRIVATE),
    MODIFY_LEVERAGE("exchangeApi/api/v1/order-mgmt/modify-leverage", PRIVATE),
    ORDER_QUERY("exchangeApi/api/v1/order-mgmt/get-order", PRIVATE),
    GET_OPEN_ORDERS("exchangeApi/api/v1/order-mgmt/get-open_orders", PRIVATE),
    GET_STOPS("exchangeApi/api/v1/order-mgmt/get-stops", PRIVATE),
    GET_FILLS("exchangeApi/api/v1/order-mgmt/get-fills", PRIVATE),
    GET_ORDER_HISTORY("exchangeApi/api/v1/order-mgmt/get-order-history", PRIVATE),
    GET_FUTURE_INFO("exchangeApi/api/v1/order-mgmt/get-future-info", PRIVATE),
    ASSET_QUERY("exchangeApi/api/v1/asset", PRIVATE),
    SYMBOL_CONFIG("exchangeApi/api/v1/symbols/symbol-config", PRIVATE),
    TRADE_DOWNLOAD_JSON("exchangeApi/api/v1/order-mgmt/trades", PRIVATE);


    public String uri;
    public ApiType apiType;

    RequestType(String uri, ApiType apiType){
        this.uri = uri;
        this.apiType = apiType;
    }
}
