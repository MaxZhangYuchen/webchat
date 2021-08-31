package com.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lombook创建构造函数
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;   //消息类型
    private String content;     //消息内容
    private String sender;      //发送者
    private String receiver;   //接收者
    //private int num;     //当前在线人数

    public enum MessageType {
        CHAT,   //消息
        JOIN,   //加入
        LEAVE   //离开
    }

}
