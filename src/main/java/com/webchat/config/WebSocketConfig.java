package com.webchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker  //使用STOMP协议
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注册一个websocket端点，客户端使用它连接到websocket服务器,并映射指定的url
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //设置客户端订阅消息的基础路径
        registry.setApplicationDestinationPrefixes("/app");
        //设置服务器广播消息的基础路径
        registry.enableSimpleBroker("/topic");
    }
}
