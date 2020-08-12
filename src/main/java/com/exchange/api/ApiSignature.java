package com.exchange.api;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ApiSignature {

    private static final Comparator<Map.Entry<String, Object>> keyComparator = Comparator.comparing(Map.Entry::getKey);

    public static String generateSign(Map<String, Object> requestData, long timestamp, String nonceStr, String apiSecret) {
        Map<String, Object> dataJson = new HashMap<>();
        Set<String> keys = requestData.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object data = requestData.get(key);

            if (data != null) {
                dataJson.put(key, requestData.get(key));
            }

        }
        dataJson.put("nonceStr", nonceStr);
        dataJson.put("timestamp", timestamp);
        dataJson.put("apiSecret", apiSecret);
        String result = dataJson.entrySet().stream().sorted(keyComparator)
                .map(entry -> entry.getKey() + "=" + entry.getValue().toString()).collect(Collectors.joining("&"));

        String sign = DigestUtils.md5Hex(result).toUpperCase();
        return sign;
    }

}
