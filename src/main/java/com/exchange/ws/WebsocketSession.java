package com.exchange.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exchange.api.ApiUtil;
import com.exchange.dto.request.ApiRequest;
import okhttp3.*;
import okio.ByteString;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static com.exchange.ws.ClientMessageType.Activate;

public class WebsocketSession extends WebSocketListener {

    private String businessNo;
    private String apiSecret;
    private volatile Long connectTime;
    private Long readyTime;
    private WebSocket socket;
    private Map<StreamKey, List<StreamListener>> listeners = new ConcurrentHashMap<>();
    private Map<StreamListener, List<Subscription>> subscriptions = new ConcurrentHashMap<>();
    private Timer timer = new Timer();
    private String authRequestTag;

    private OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build();

    static final String API_HOST = System.getProperty("moonx.api.host", "exchange.moonx.pro");
    static final boolean API_SSL = Boolean.parseBoolean(System.getProperty("moonx.api.ssl", "true"));
    static final String API_URL = (API_SSL ? "wss" : "ws") + "://" + API_HOST + "/stream";
    static final long RECONNECTION_DELAY = 2000L;

    public WebsocketSession(String businessNo, String apiSecret) {
        this.businessNo = businessNo;
        this.apiSecret = apiSecret;
        connect();
    }

    WebsocketSession connect() {
        log("Attempting to connect");
        Request request = new Request.Builder().url(API_URL).build();
        try {
            socket = client.newWebSocket(request, this);
        } catch (Exception e) {
            e.printStackTrace();
            delay(RECONNECTION_DELAY, this::reconnect);
        }
        return this;
    }

    void disconnect() throws Exception {
        log("Disconnecting Socket");
        socket.close(0, "");
    }

    private void reconnect() {
        log("Attempting to reconnect");
        connect();
    }

    public synchronized void unSubscribe(Subscription s, StreamListener l) {
        boolean sendUnsubscribe = false;
        List<StreamListener> streamListeners = listeners.get(s.streamKey);
        if(streamListeners != null){
            streamListeners.remove(l);
            if(streamListeners.size() == 0){
                listeners.remove(s.streamKey);
                sendUnsubscribe = true;
            }
        }
        List<Subscription> subs = subscriptions.get(l);
        if(subs != null) {
            subs.remove(s);
            if (subs.size() == 0) {
                subscriptions.remove(l);
            }
        }
        if (ready() && sendUnsubscribe) {
            String json = s.unsubscribeJson();
            socket.send(json);
            log("unSubscribed  " + json);
        }
    }

    private boolean ready() {
        return readyTime != null;
    }

    public synchronized void subscribe(Subscription s, StreamListener l) {
        log("subscribe");
        listeners.computeIfAbsent(s.streamKey, key -> new CopyOnWriteArrayList<>()).add(l);
        subscriptions.computeIfAbsent(l, key -> new CopyOnWriteArrayList<>()).add(s);
        if (ready()) {
            String json = s.subscribeJson();
            socket.send(json);
            log("subscribed :: " + json);
        }
    }

    public synchronized void unSubscribeAll(StreamListener l) {
        log("unSubscribeAll" + l.getClass().getSimpleName());
        Optional.ofNullable(subscriptions.remove(l)).ifPresent(subs -> {
            for (Subscription s : subs) {
                List<StreamListener> list = listeners.get(s.streamKey);
                list.remove(l);
                if (list.size() == 0) {
                    if (ready()) {
                        socket.send(s.unsubscribeJson());
                    }
                }
                log("unSubscribe" + l.getClass().getSimpleName() + ", streamKey: " + s.streamKey);
            }
        });
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log("Connected");
        connectTime = System.currentTimeMillis();
        socket = webSocket;
        authRequestTag = "auth-" + System.currentTimeMillis();
        //Authenticating the socket once it is open
        ApiRequest apiRequest = new ApiUtil().apiRequest(null, businessNo, apiSecret);
        String json = new JSONObject()
                .fluentPut(Field.Type.value(), ClientMessageType.ApiAuth.value())
                .fluentPut(Field.Tag.value(), authRequestTag)
                .fluentPut(Field.Data.value(), JSONObject.toJSON(apiRequest))
                .toJSONString();
        socket.send(json);
    }

    private void refreshSubscriptions() {
        subscriptions.values().stream()
                .flatMap(Collection::stream)
                .distinct()
                .forEach(subscription -> socket.send(subscription.subscribeJson()));
        readyTime = System.currentTimeMillis();
    }

    public void onMessage(WebSocket webSocket, String text) {
        JSONObject o = JSON.parseObject(text);
        ServerMessageType type = ServerMessageType.parse(o.getString(Field.Type.value()));
        switch (type) {
            case Snapshot:
                log("message :: " + o);
            case DeltaUpdate:
                StreamKey streamKey = StreamKey.parse(o.getString(Field.StreamName.value()));
                Message msg = new Message(type, streamKey, o.getLong(Field.SeqNo.value()), o.get(Field.Data.value()));
                Optional.ofNullable(listeners.get(streamKey))
                        .ifPresent(ls -> ls.forEach(l -> l.data(msg)));
                break;
            case ActivateAlert:
                socket.send(new JSONObject()
                        .fluentPut(Field.Type.value(), Activate.value())
                        .fluentPut(Field.Time.value(), o.getString(Field.Time.value()))
                        .toJSONString()
                );
                break;
            case Response:
                String requestTag = o.getString(Field.Tag.value());
                Status status = Status.parse(o.getInteger(Field.Status.value()));
                if (requestTag.equals(authRequestTag) && status == Status.Success) {
                    //Republishing subscriptions
                    refreshSubscriptions();
                }
        }
    }

    public void onMessage(WebSocket webSocket, ByteString bytes) {
        onMessage(webSocket, bytes.utf8());
    }

    public void onClosing(WebSocket webSocket, int code, String reason) {
        log("socket closing. code: " + code + ", reason: " + reason);
    }

    public void onClosed(WebSocket webSocket, int code, String reason) {
        log("socket closed. code: " + code + ", reason: " + reason);
        subscriptions.keySet().forEach(StreamListener::disconnected);
        connectTime = null;
        readyTime = null;
        socket = null;
        authRequestTag = null;
        delay(RECONNECTION_DELAY, this::reconnect);
    }

    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
        log("CallbackError. msg: " + t.getMessage() + ", response: " + response);
    }

    private void delay(long milliSeconds, Runnable task) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, milliSeconds);
    }

    private void log(String text) {
        System.out.println("WS :: " + text);
    }
}
