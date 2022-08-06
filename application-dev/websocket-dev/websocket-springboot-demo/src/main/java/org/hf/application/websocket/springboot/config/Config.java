package org.hf.application.websocket.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author:hufei
 * @CreateTime:2020-09-30
 * @Description:开启websocket端点
 */
@Configuration
public class Config {

    /**
    *@params: []
    *@return: ServerEndpointExporter
    *@description: 向spring中注入一个webSocket端点
    *@author: hufei
    *@time: 2020/10/10 10:16
    */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return  new ServerEndpointExporter();
    }
}
