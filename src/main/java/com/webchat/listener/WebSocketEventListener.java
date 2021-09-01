package com.webchat.listener;

import com.webchat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    //记录连接到websocket的人数，反馈在log中
    public int onlineUser;

    public void setOnlineUser(final int userNumber) {
        this.onlineUser = onlineUser;
    }
    public int getOnlineUser() {
        return onlineUser;
    }


    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event, SimpMessageHeaderAccessor headerAccessor) {
        onlineUser++;
        logger.info(onlineUser + "连接到聊天室");
        ChatMessage chatMessage = new ChatMessage();
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend("/topic/public", chatMessage);

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        onlineUser--;
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            logger.info("用户: " + username + " 断开连接 "+ "当前在线人数: "+onlineUser);
            ChatMessage chatMessage = new ChatMessage();

            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setNum(onlineUser);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);

        }
    }



}
