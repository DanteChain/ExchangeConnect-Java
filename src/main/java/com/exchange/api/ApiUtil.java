package com.exchange.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.exchange.dto.request.ApiRequest;
import com.exchange.dto.response.ApiResponse;
import com.exchange.enums.RequestType;
import okhttp3.*;
import okhttp3.OkHttpClient.Builder;
import okio.BufferedSink;
import okio.Okio;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.exchange.api.ApiSignature.generateSign;

public class ApiUtil {

    static final int CONN_TIMEOUT = 10;
    static final int READ_TIMEOUT = 60;
    static final int WRITE_TIMEOUT = 60;


    static final String API_HOST = System.getProperty("moonx.api.host", "exchange.moonx.pro");
    static final boolean API_SSL = Boolean.parseBoolean(System.getProperty("moonx.api.ssl", "true"));
    static final String API_URL = (API_SSL ? "https" : "http") + "://" + API_HOST;

    static final MediaType JSON = MediaType.parse("application/json");


    static final OkHttpClient client = createOkHttpClient();

    public <T> ApiResponse<T> sendRequest(RequestType requestType, Object data, String businessNo, String key, Class<T> responseType) throws IOException {

        RequestBody body = getRequestBody(data, businessNo, key);
        Request.Builder builder = new Request.Builder().url(API_URL + "/" + requestType.uri).post(body);
        Request request = builder.build();
        System.out.println("Request: "+ requestType);
        Response response = client.newCall(request).execute();
        ApiResponse resDto = JSONObject.parseObject(response.body().string(), ApiResponse.class);
        return resDto;
    }

    public File sendRequest_(RequestType requestType, Object data, String businessNo, String key) throws IOException {

        RequestBody body = getRequestBody(data, businessNo, key);
        String respType = "application/vnd.ms-excel";
        Request.Builder builder = new Request.Builder().url(API_URL + "/" + requestType.uri).post(body).header("Accept", respType).header("Content-Type","application/json");
        Request request = builder.build();
        System.out.println("Request: "+ requestType);
        Response response = client.newCall(request).execute();

        File downloadedFile= new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString() + ".xlsx");

        if (MediaType.parse(respType).equals(response.body().contentType())){
            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();
        }
        return downloadedFile;
    }

    private RequestBody getRequestBody(Object data, String businessNo, String key) {
        ApiRequest apiRequestDto = apiRequest(data, businessNo, key);
        String jsonData = JSONObject.toJSONString(apiRequestDto, SerializerFeature.WriteMapNullValue,
                SerializerFeature.QuoteFieldNames, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteEnumUsingName);
        return RequestBody.create(JSON, jsonData);
    }

    public ApiRequest apiRequest(Object data, String businessNo, String key) {
        String nonceStr = RandomStringUtils.randomNumeric(32);
        int timestamp = (int) (System.currentTimeMillis() / 1000L);
        String sign;
        if (data != null) {
            JSONObject dataMap = (JSONObject) JSONObject.toJSON(data);
            sign = generateSign(dataMap, timestamp, nonceStr, key);
        } else {
            JSONObject jsonObject = new JSONObject();
            sign = generateSign(jsonObject, timestamp, nonceStr, key);
        }

        ApiRequest apiRequestDto = new ApiRequest();
        apiRequestDto.setData(data);
        apiRequestDto.setNonceStr(nonceStr);
        apiRequestDto.setTimestamp(timestamp);
        apiRequestDto.setSign(sign);
        apiRequestDto.setBusinessNo(businessNo);

        return apiRequestDto;
    }

    public JSONObject sendRequest(RequestType requestType, Map<String, String> params) throws IOException {

        HttpUrl.Builder httpBuider = HttpUrl.parse(API_URL + "/" + requestType.uri).newBuilder();
        if (params != null) {
            for(Map.Entry<String, ?> param : params.entrySet()) {
                httpBuider.addEncodedQueryParameter(param.getKey(),param.getValue().toString());
            }
        }
        System.out.println("Request: "+ requestType);
        Request request = new Request.Builder().url(httpBuider.build()).build();
        Response response = client.newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }

    static OkHttpClient createOkHttpClient() {
        return new Builder().connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

}
