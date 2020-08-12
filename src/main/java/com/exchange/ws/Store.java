package com.exchange.ws;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exchange.domain.*;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Store implements StreamListener {

    private static final Comparator<MarketTrade> marketTradeComparator = Comparator.comparing(MarketTrade::getTime).reversed();

    Map<String, UserPosition> userPositions = new ConcurrentHashMap<>();
    Map<String, UserFill> userFills = new ConcurrentHashMap<>();
    Map<String, UserOrder> userOrders = new ConcurrentHashMap<>();
    Map<String, UserAsset> userAssets = new ConcurrentHashMap<>();
    Map<String, LinkedList<MarketTrade>> trades = new ConcurrentHashMap<>();
    Map<String, MarketStat> stats = new ConcurrentHashMap<>();
    List<StoreListener> listeners = new CopyOnWriteArrayList<>();



    @Override
    public void delta(Message msg) {
        switch (msg.streamKey().getFlavour()) {
            case Trade:
                String symbol = msg.streamKey.getSubKey().toUpperCase();
                MarketTrade newTrade = msg.<JSONObject>data().toJavaObject(MarketTrade.class);
                Optional.ofNullable(trades.get(symbol))
                        .ifPresent(l -> {
                            l.removeLast();
                            l.addFirst(newTrade);
                        });
                trades.putIfAbsent(symbol, Stream.of(newTrade).collect(Collectors.toCollection(LinkedList::new)));
                System.out.println(trades.toString());
                break;
            case Index:
                break;
            case UserOrder:
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.toJavaObject(UserOrder.class))
                        .ifPresent(o -> {
                            userOrders.put(o.getSymbol(), o);
                            notifyListener(l -> l.userOrder(o));
                        });
                break;
            case UserTrade:
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.toJavaObject(UserFill.class))
                        .ifPresent(o -> {
                            userFills.put(o.getSymbol(), o);
                            notifyListener(l -> l.userFill(o));
                        });
                break;
            case FutureInfo:
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.toJavaObject(UserPosition.class))
                        .ifPresent(o -> {
                            userPositions.put(o.getSymbol(), o);
                            notifyListener(l -> l.userPosition(o));
                        });
                break;
            case UserAsset:
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.toJavaObject(UserAsset.class))
                        .ifPresent(o -> {
                            userAssets.put(o.getAssetCode(), o);
                            notifyListener(l -> l.userAsset(o));
                        });
                break;
        }
    }

    @Override
    public void snapshot(Message msg) {
        switch (msg.streamKey().getFlavour()) {
            case OrderStack:
                break;
            case Trade:
                trades.clear();
                LinkedList<MarketTrade> list = msg.<JSONArray>data()
                        .stream()
                        .map(x -> (JSONObject) x)
                        .map(x -> x.toJavaObject(MarketTrade.class))
                        .collect(Collectors.toCollection(LinkedList::new));
                trades.put(msg.streamKey.getSubKey().toUpperCase(), list);
                System.out.println(trades.toString());
                break;
            case Kline:
                break;
            case Stat:
                break;
            case Index:
                break;
            case UserOrder:
                userOrders.clear();
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.getJSONArray("list"))
                        .ifPresent(o -> o.stream()
                                .map(x -> (JSONObject) x)
                                .map(x -> x.toJavaObject(UserOrder.class))
                                .forEach(x -> userOrders.put(x.getSymbol(), x))
                        );
                break;
            case UserTrade:
                userFills.clear();
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.getJSONArray("list"))
                        .ifPresent(o -> o.stream()
                                .map(x -> (JSONObject) x)
                                .map(x -> x.toJavaObject(UserFill.class))
                                .forEach(x -> userFills.put(x.getSymbol(), x))
                        );
                break;
            case FutureInfo:
                userPositions.clear();
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.getJSONArray("list"))
                        .ifPresent(o -> o.stream()
                                .map(x -> (JSONObject) x)
                                .map(x -> x.toJavaObject(UserPosition.class))
                                .forEach(x -> userPositions.put(x.getSymbol(), x))
                        );
                break;
            case UserAsset:
                userAssets.clear();
                Optional.ofNullable(msg.<JSONObject>data())
                        .map(o -> o.getJSONArray("list"))
                        .ifPresent(o -> o.stream()
                                .map(x -> (JSONObject) x)
                                .map(x -> x.toJavaObject(UserAsset.class))
                                .forEach(x -> userAssets.put(x.getAssetCode(), x))
                        );
                break;
        }
    }

    @Override
    public void disconnected() {

    }

    public void addListener(StoreListener listener){
        listeners.add(listener);

    }

    public void removeListener(StoreListener listener){
        listeners.remove(listener);
    }

    private void notifyListener(Consumer<StoreListener> listenerConsumer){
        listeners.forEach(listenerConsumer);
    }
}
