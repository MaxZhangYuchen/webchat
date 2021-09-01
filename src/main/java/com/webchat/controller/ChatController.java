package com.webchat.controller;

import com.webchat.entity.ChatMessage;
import com.webchat.listener.WebSocketEventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController extends WebSocketEventListener{


    //群发
    @MessageMapping("/chat.sendMessage")        //MessageMapping接受客户端信息
    @SendTo("/topic/public")                    //SendTo广播出去
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("funtion = sendMessage");
        return getChatMessage(chatMessage);
    }

    /**
     * 打印 chatmessage信息，转发chatmessage
     *
     * @param chatMessage
     * @return
     */
    public ChatMessage getChatMessage(@Payload ChatMessage chatMessage) {
        System.out.println("type= " + chatMessage.getType());
        System.out.println("username= " + chatMessage.getSender());
        System.out.println("receiver= " + chatMessage.getReceiver());
        System.out.println("userOfNum= " + chatMessage.getNum());
        System.out.println("-----------------------------");
        return chatMessage;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        WebSocketEventListener webSocketEventListener;
        int num = webSocketEventListener.getOnlineUser();
        num++;
        webSocketEventListener.setOnlineUser(num);

        System.out.println("funtion = addUser");
        System.out.println("headerAccessor= " + headerAccessor.getSessionAttributes());
        System.out.println("onlineUser= " + webSocketEventListener.getOnlineUser());

        return getChatMessage(chatMessage);
    }





}
