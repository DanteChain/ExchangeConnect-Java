package com.exchange.ws;

public interface StreamListener {
    void delta(Message msg);

    void snapshot(Message msg);

    default void disconnected() {}

    default void data(Message msg) {
        if (msg.type() == ServerMessageType.Snapshot) {
            snapshot(msg);
        } else {
            delta(msg);
        }
    }
}