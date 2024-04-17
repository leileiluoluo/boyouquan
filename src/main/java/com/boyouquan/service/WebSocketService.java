package com.boyouquan.service;

import com.boyouquan.model.WebSocketMessage;

public interface WebSocketService {

    void broadcast(WebSocketMessage message);

}
