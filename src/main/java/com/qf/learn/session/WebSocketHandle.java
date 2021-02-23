package com.qf.learn.session;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.qf.learn.Config;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket", configurator = WebSocketConfig.class)
@Component
public class WebSocketHandle {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession http = (HttpSession) config.getUserProperties().get(Config.WEB_SOCKET);
        BindHttpSession(session, http);
        System.out.println("websocketopen:" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        UnBindHttpSession(session);
        System.out.println("websocketclose:" + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        UnBindHttpSession(session);
        System.out.println("websocketerror:" + session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(message);
    }

    private void BindHttpSession(Session session, HttpSession http) {
        try {
            // 相互绑定数据
            http.setAttribute(Config.WEB_SOCKET, session);
            session.getUserProperties().put(Config.WEB_SOCKET, http);
        } catch (Exception e) {
        }
    }

    private void UnBindHttpSession(Session session) {
        try {
            HttpSession http = (HttpSession) session.getUserProperties().get(Config.WEB_SOCKET);
            http.removeAttribute(Config.WEB_SOCKET);
            session.close();
        } catch (Exception e) {
        }
    }
}
