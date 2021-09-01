package com.webchat.controller;

import com.webchat.entity.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Controller
public class ChatController {

    //生成日志
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    //记录登录的人数
    public int onlineUser;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * socket创建事件监听
     * @param event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        onlineUser++;
        logger.info("当前在线人数: "+onlineUser);
    }

    /**
     * socket断开连接监听
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        onlineUser--;
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        //用户离开，发送用户离开消息
        if(username != null) {
            logger.info("用户: " + username + " 断开连接 "+ "当前在线人数: "+onlineUser);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setNum(onlineUser);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    /**
     * 消息转发
     * @param chatMessage
     * @return
     */
    @MessageMapping("/chat.sendMessage")        //MessageMapping接受客户端信息
    @SendTo("/topic/public")                    //SendTo广播出去
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
    }


    /**
     * 添加用户
     * @param chatMessage
     * @param headerAccessor
     * @return
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setNum(onlineUser);
        return chatMessage;
    }


}
