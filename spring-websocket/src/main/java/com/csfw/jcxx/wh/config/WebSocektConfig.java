package com.csfw.jcxx.wh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @USER wh
 * @DATE 2020/6/8
 * @Description  websocket 配置类
 */
@Configuration
public class WebSocektConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
