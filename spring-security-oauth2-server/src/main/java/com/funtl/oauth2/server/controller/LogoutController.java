package com.funtl.oauth2.server.controller;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LogoutController {

    @RequestMapping("oauth/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            System.out.println(request.getHeader("Authorization"));
            response.sendRedirect(request.getHeader("Authorization"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
