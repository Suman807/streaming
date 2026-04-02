package com.sb.video.streaming.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sb.video.streaming.model.Role;
import com.sb.video.streaming.model.User;
import com.sb.video.streaming.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        User user = userService.getUserByName(username);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        
        if (user.getRole() == Role.ADMIN){
            response.sendRedirect("/admin");
        }
        else{
            response.sendRedirect("/user/dashboard");
        }
    }

}
