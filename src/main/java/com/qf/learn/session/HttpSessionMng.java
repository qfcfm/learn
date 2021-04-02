package com.qf.learn.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.Session;

import com.qf.learn.Config;
import com.qf.learn.Model.User;

@WebListener
public class HttpSessionMng implements HttpSessionListener {

    // 已登录用户信息
    private static Map<String, HttpSession> users = new HashMap<String, HttpSession>();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("创建httpsession:" + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        NotifyWebSocket(se.getSession(), "timeout");
        System.out.println("销毁httpsession:" + se.getSession().getId());
    }

    public static void NotifyWebSocket(HttpSession session, String type) {
        Session websocket = (Session) session.getAttribute(Config.WEB_SOCKET);
        session.removeAttribute(Config.WEB_SOCKET);
        try {
            if (websocket != null) {
                websocket.getBasicRemote().sendText("{\"type\":\"" + type + "\"}");
                websocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UserLogin(HttpSession session, User user) {
        // 查找之前登录的账户,并踢掉
        try {
            HttpSession oldsession = users.get(user.getUsername());
            if (oldsession != null) {
                NotifyWebSocket(oldsession, "userloginsite");
                oldsession.removeAttribute(Config.USER_KEY);
            }
        } catch (Exception e) {
        }
        // 用户加入已登录队列
        session.setAttribute(Config.USER_KEY, user);
        users.put(user.getUsername(), session);
    }

    public static void UserLogout(HttpSession session, User user) {
        // 移除已登录队列
        User tmp = (User) session.getAttribute(Config.USER_KEY);
        if (tmp != null) {
            users.remove(tmp.getUsername());
        }
        // 用户移除,并通知
        session.removeAttribute(Config.USER_KEY);
        NotifyWebSocket(session, "userlogout");
    }

    public static boolean UserIsLogin(HttpSession session) {
        User tmp = (User) session.getAttribute(Config.USER_KEY);
        if (tmp != null) {
            return true;
        }
        return false;
    }

    public static User CurUser(HttpSession session) {
        User tmp = (User) session.getAttribute(Config.USER_KEY);
        if (tmp != null) {
            return tmp;
        }
        return null;
    }

    public static boolean UserIsLogin(HttpSession session) {
        User tmp = (User) session.getAttribute(Config.USER_KEY);
        if (tmp != null) {
            return true;
        }
        return false;
    }

    public static User CurUser(HttpSession session) {
        User tmp = (User) session.getAttribute(Config.USER_KEY);
        if (tmp != null) {
            return tmp;
        }
        return null;
    }
}
