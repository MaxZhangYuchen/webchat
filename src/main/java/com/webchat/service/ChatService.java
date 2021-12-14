package com.webchat.service;

import com.webchat.controller.ChatController;
import com.webchat.entity.ChatMessage;
import com.webchat.mapper.UserMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChatService {

    @Resource
    private UserMapper userMapper;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ChatController.class);

    public int saveMessage(ChatMessage chatMessage){
        int result = userMapper.insertChatMessage(chatMessage);

        if(result>0){
            logger.info("消息写入数据库");
        }
        else{
            logger.info("消息写入失败");
        }
        return result;
    }
}
