package org.hf.application.websocket.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启websocket端点
 * @author HF
 */
@Configuration
public class WebSocketConfig {

    /**
    * 向spring中注入一个webSocket端点
    */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
