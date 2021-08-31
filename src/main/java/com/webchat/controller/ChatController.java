package com.webchat.controller;

import com.webchat.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {


    public int userOfNum = 0;
    //群发
    @MessageMapping("/chat.sendMessage")        //MessageMapping接受客户端信息
    @SendTo("/topic/public")                    //SendTo广播出去
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){

        if(chatMessage.getType()== ChatMessage.MessageType.LEAVE){
            userOfNum--;
            System.out.println(userOfNum);
        }
        //chatMessage.setNum(userOfNum);
        System.out.println("funtion = sendMessage");
        return getChatMessage(chatMessage);
    }

    public ChatMessage getChatMessage(@Payload ChatMessage chatMessage) {
        System.out.println("type= " + chatMessage.getType());
        System.out.println("username= " + chatMessage.getSender());
        System.out.println("receiver= " + chatMessage.getReceiver());
        //System.out.println("userOfNum= " + chatMessage.getNum());
        System.out.println("-----------------------------");

        return chatMessage;
    }

    //添加用户
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        userOfNum++;
        //chatMessage.setNum(userOfNum);
        System.out.println("funtion = addUser");
        System.out.println("headerAccessor= " + headerAccessor.getSessionAttributes());
        return getChatMessage(chatMessage);
    }






}
