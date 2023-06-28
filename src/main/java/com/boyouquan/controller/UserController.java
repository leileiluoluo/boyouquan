package com.boyouquan.controller;

import com.boyouquan.model.UserInfo;
import com.boyouquan.model.UserLogin;
import com.boyouquan.model.UserRegister;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register-action")
    public String registerAction(UserRegister userRegister) {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login-action")
    public String loginAction(UserLogin userLogin, HttpSession session) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("olzhy@qq.com");
        userInfo.setNickname("磊磊落落");
        session.setAttribute("user", userInfo);
        return "redirect:/home";
    }

    @PostMapping("/logout-action")
    public String logoutAction(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/home";
    }

    @GetMapping("/center")
    public String center(Model model) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("olzhy@qq.com");
        userInfo.setNickname("磊磊落落");

        model.addAttribute("userInfo", userInfo);
        return "user/center";
    }

}
