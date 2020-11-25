package com.qf.learn.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.qf.learn.Config;
import com.qf.learn.Model.User;
import com.qf.learn.session.HttpSessionMng;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/API")
public class RestAPI {

    @RequestMapping(value = "**")
    public String api() {
        return "NO DATA";
    }

    @RequestMapping(value = "/init")
    public String init(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User ac = (User) session.getAttribute(Config.USER_KEY);
        if (ac != null) {
            return ac.toString();
        } else {
            return "error";
        }
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, @RequestBody Map<String, String> json) {
        HttpSession session = request.getSession();
        String name = json.get("username");
        // String pwd = json.get("password");

        User user = new User();
        user.setRole("secadmin");
        user.setUsername(name);

        HttpSessionMng.UserLogin(session, user);
        return user.toString();
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        HttpSessionMng.UserLogout(session, null);
        return "";
    }
}