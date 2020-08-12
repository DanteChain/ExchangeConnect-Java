package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum StreamFlavour {
    OrderStack("stack", false, true, false),
    Trade("trade",true, true, false),
    Kline("kline", true, true, false),
    Stat("stat", false, false, false),
    Announce("announce", true, true, false),
    Index("index", false, false, false),
    UserOrder("user-order", true, false, true),
    UserTrade("user-trade", true, false, true),
    FutureInfo("user-future", true, false, true),
    UserAsset("user-asset", true, false, true);

    private static Map<String, StreamFlavour> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(StreamFlavour::getFlavorKey, Function.identity()));

    String flavorKey;
    boolean incremental;
    boolean hasSubKey;
    boolean authenticated;

    public static StreamFlavour parse(String flavorKey) {
        return Optional.ofNullable(MAP.get(flavorKey)).orElseThrow(() -> new IllegalStateException("Invalid StreamFlavor :: " + flavorKey));
    }

}
