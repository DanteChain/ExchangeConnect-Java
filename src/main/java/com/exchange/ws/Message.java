package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class Message {
    ServerMessageType type;
    StreamKey streamKey;
    Long sequence;
    Object data;

    <T> T data() {
        return (T) data;
    }
}
