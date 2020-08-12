package com.exchange.ws;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StreamKey {
    private StreamFlavour flavour;
    private String subKey;
    private String key;

    public static StreamKey parse(String key){
        String[] arr = key.split("@");
        StreamFlavour flavour = StreamFlavour.parse(arr[0]);
        if(flavour.hasSubKey){
            return new StreamKey(flavour, arr[1]);
        }
        return new StreamKey(flavour);
    }

    public StreamKey(StreamFlavour flavour, String dataKey) {
        this.flavour = flavour;
        this.subKey = dataKey;
        this.key = this.flavour.getFlavorKey() + "@" + this.subKey;
    }

    public StreamKey(StreamFlavour flavour){
        this.flavour = flavour;
        this.key = flavour.getFlavorKey();
    }

}
