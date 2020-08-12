package com.exchange.ws;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Field {
    Type("T"),
    Time("t"),
    StreamName("S"),
    SeqNo("s"),
    Data("D"),
    Key("key"),
    Symbol("symbol"),
    Tag("tag"),
    Status("status");;

    private String value;

    public String value(){
        return value;
    }
}
