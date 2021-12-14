package com.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//lombok创建构造函数
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public ChatMessage(ChatMessage chatMessage) {
    }

    public enum MessageType {
        CHAT,   //消息
        JOIN,   //加入
        LEAVE   //离开
    }
    private MessageType type;   //消息类型
    private String content;     //消息内容
    private String sender;      //发送者
    private String receiver;    //接收者
    private int num;            //当前在线人数
    private String sendTime;      //发送时间


}
