package com.boyouquan.service.impl;

import com.boyouquan.model.WebSocketMessage;
import com.boyouquan.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void broadcast(WebSocketMessage message) {
        template.convertAndSend("/topic/broadcasts", message);
    }

    @Scheduled(fixedRate = 2000)
    public void test() {
        System.out.println("hahaha");
        WebSocketMessage message = new WebSocketMessage();
        message.setMessage("hello");
        broadcast(message);
    }

}
