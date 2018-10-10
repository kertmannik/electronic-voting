package com.web_application_development.evoting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getSmartIdLoginPage() {
        return "login/index";
    }

    @GetMapping(path = "/secured")
    public String getSecuredPage() {
        return "secured/test";
    }
}
