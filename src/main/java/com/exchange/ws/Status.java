package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public enum Status {
    Success(0),
    Error(1),


    SubscriptionFailed(1001),
    UnSubscriptionFailed(1002),


    AuthFailed(2001),
    AuthMissing(2002);

    private static Map<Integer, Status> MAP = Arrays.stream(values()).collect(Collectors.toMap(Status::id, Function.identity()));

    private int id;

    public static Status parse(int id){
        return MAP.get(id);
    }
}
