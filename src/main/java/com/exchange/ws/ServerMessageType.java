package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public enum ServerMessageType {
    Snapshot("s"),
    DeltaUpdate("u"),
    ActivateAlert("activate-alert"),
    Response("response");

    private static Map<String, ServerMessageType> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ServerMessageType::value, Function.identity()));


    private String value;

    public static ServerMessageType parse(String value) {
        return Optional.ofNullable(MAP.get(value)).orElseThrow(() -> new IllegalStateException("Invalid Type :: " + value));
    }
}
