package com.boyouquan.service.impl;

import com.boyouquan.model.WebSocketMessage;
import com.boyouquan.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void broadcast(WebSocketMessage message) {
        template.convertAndSend("/topic/broadcasts", message);
    }

}
