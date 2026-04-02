package com.sb.video.streaming.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sb.video.streaming.model.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@Controller
@Tag(name = "User APIs", description = "APIs related to user dashboard and session info")
public class UserController implements UserBaseController{
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("username", user.getUsername());
        return "dashboard"; // Thymeleaf view
    }

    // @GetMapping("/session-info")
    // @ResponseBody
    // public String sessionInfo(HttpSession session) {
    //     User user = (User) session.getAttribute("user");
    //     return "Session ID: " + session.getId() + " | User: " + user.getUsername();
    // }

    @GetMapping("/session-info")
    @ResponseBody
    public String sessionInfo(Authentication auth, HttpSession session) {
        User user = (User) auth.getPrincipal();
        return "Session ID: " + session.getId() + " | User: " + user.getUsername();
    }

}
