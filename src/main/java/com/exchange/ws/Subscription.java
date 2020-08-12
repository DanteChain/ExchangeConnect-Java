package com.exchange.ws;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

import static com.exchange.ws.ClientMessageType.Subscribe;
import static com.exchange.ws.ClientMessageType.UnSubscribe;

public class Subscription {
    StreamKey streamKey;
    long throttleMillis;
    JSONObject context = new JSONObject();

    public Subscription streamKey(StreamKey streamKey) {
        this.streamKey = streamKey;
        put(Field.Key.value(), streamKey.getKey());
        return this;
    }

    public Subscription throttleMillis(long throttleMillis) {
        this.throttleMillis = throttleMillis;
        put("throttleMillis", throttleMillis);
        return this;
    }

    public Subscription put(String key, Object value) {
         context.put(key, value);
        return this;
    }

    String subscribeJson() {
        return new JSONObject()
                .fluentPut(Field.Type.value(), Subscribe.value())
                .fluentPut(Field.Data.value(), context)
                .toJSONString();
    }

    String unsubscribeJson() {
        return new JSONObject()
                .fluentPut(Field.Type.value(), UnSubscribe.value())
                .fluentPut(Field.Data.value(), new JSONObject()
                        .fluentPut(Field.Key.value(), streamKey.getKey())
                )
                .toJSONString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return streamKey.equals(that.streamKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamKey);
    }

}