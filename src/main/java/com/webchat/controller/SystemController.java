package com.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    /**
     * 调到登录界面
     * @return
     */
    @RequestMapping("/")
    public String root(){return "redirect:/login";}
    /**
     * 登录页面跳转
     * */
    @GetMapping("login")
    public String login(){
        return "login";  //thymeleaf 模板引擎
    }

    @GetMapping("registry")
    public String registry(){
        return "registry";  //thymeleaf 模板引擎
    }
}
