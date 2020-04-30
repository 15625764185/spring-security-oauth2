package com.oauth2.client.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Usercontroller {

    @GetMapping("/view")
    @PreAuthorize("hasAuthority('SystemUserView')")
    public String normal( ) {
        return "view页面";
    }

    @GetMapping("/insert")
    public String medium() {
        return "SystemUserInsert页面";
    }

    @GetMapping("/update")
    public String admin() {
        return "Update页面";
    }
}
