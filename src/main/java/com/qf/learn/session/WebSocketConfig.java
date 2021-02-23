package com.qf.learn.session;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.qf.learn.Config;

/**
 * 开启WebSocket支持
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            //System.out.println(httpSession.getId());
            sec.getUserProperties().put(Config.WEB_SOCKET, httpSession);
        } catch (Exception e) {
        }

    }
}