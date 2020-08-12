package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public enum ClientMessageType {
    Subscribe("sub"),
    UnSubscribe("unsub"),
    Activate("activate"),
    ApiAuth("api-auth");

    private String value;

}

