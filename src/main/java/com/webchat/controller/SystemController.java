package com.webchat.controller;

import com.webchat.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    /**
     * 自动跳到登录界面
     * @return
     */
    @RequestMapping("/")
    public String root(){return "redirect:/login";}
    /**
     * 登录页面
     * */
    @GetMapping("login")
    public String login(){
        return "login";
    }

    /**
     * 注册页面
     * @return
     */
    @GetMapping("registry")
    public String registry(){
        return "registry";
    }

    /**
     * 聊天室主页
     * @return
     */
    @RequestMapping("chat")
    public String chatRoom(){return "chat";}

}
