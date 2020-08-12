package com.exchange.api;

import com.alibaba.fastjson.JSONObject;
import com.exchange.dto.request.*;
import com.exchange.dto.response.ApiResponse;
import com.exchange.enums.RequestType;
import com.exchange.ws.Store;
import com.exchange.ws.Subscription;
import com.exchange.ws.WebsocketSession;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.HashMap;

public class ApiClient {

    private final String businessNo;
    private final String apiSecret;
    private WebsocketSession websocketSession;
    @Getter
    @Accessors(fluent = true)
    private Store store;

    public ApiClient(String businessNo, String apiSecret){
        this.businessNo = businessNo;
        this.apiSecret = apiSecret;
        this.websocketSession = new WebsocketSession(businessNo, apiSecret);
        this.store = new Store();
    }

    public ApiResponse<String> createNewOrder(OrderRequest orderRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.NEW_ORDER, orderRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> modifyOrder(OrderRequest modifyOrderRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.MODIFY_ORDER, modifyOrderRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> modifyLeverage(ModifyLeverageDto modifyLeverageDto) throws IOException {
        return new ApiUtil().sendRequest(RequestType.MODIFY_LEVERAGE, modifyLeverageDto, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> cancelOrder(CancelOrderRequest cancelOrderRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.CANCEL_ORDER, cancelOrderRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getOrderDetails(OrderQueryRequest orderQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.ORDER_QUERY, orderQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getOpenOrders(OrderQueryRequest orderQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.GET_OPEN_ORDERS, orderQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getStops(OrderQueryRequest orderQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.GET_STOPS, orderQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getFills(OrderQueryRequest orderQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.GET_FILLS, orderQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getOrderHistory(OrderQueryRequest orderQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.GET_ORDER_HISTORY, orderQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> assetQuery(AssetQueryRequest assetQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.ASSET_QUERY, assetQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> getFutureInfo(AssetQueryRequest assetQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.GET_FUTURE_INFO, assetQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> symbols(SymbolQueryRequest symbolQueryRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.SYMBOL_CONFIG, symbolQueryRequest, this.businessNo, this.apiSecret, String.class);
    }

    public ApiResponse<String> tradeDataAsJSON(TradeDownloadRequest tradeDownloadRequest) throws IOException {
        return new ApiUtil().sendRequest(RequestType.TRADE_DOWNLOAD_JSON, tradeDownloadRequest, this.businessNo, this.apiSecret, String.class);
    }

    public void subscribe(Subscription subscription) {
        websocketSession.subscribe(subscription, store);
    }


    public void unSubscribe(Subscription subscription) {
        websocketSession.unSubscribe(subscription, store);
    }

    public static JSONObject depth(String symbol) throws IOException {
        HashMap params = new HashMap();
        params.put("symbol", symbol);
        return new ApiUtil().sendRequest(RequestType.DEPTH, params);
    }

    public static JSONObject tradeHistory(String symbol) throws IOException {
        HashMap params = new HashMap();
        params.put("symbol", symbol);
        return new ApiUtil().sendRequest(RequestType.TRADE_HISTORY, params);
    }

    public static JSONObject marketTickers() throws IOException {
        return new ApiUtil().sendRequest(RequestType.TICKERS, null);
    }

    public static JSONObject kline(String symbol, long startTime, long endTime, long pageSize, String kline) throws IOException {
        HashMap params = new HashMap();
        params.put("symbol", symbol);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("pageSize", pageSize);
        params.put("kline", kline);
        return new ApiUtil().sendRequest(RequestType.KLINE, params);
    }
}
