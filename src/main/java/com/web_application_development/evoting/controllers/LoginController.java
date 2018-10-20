package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.services.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MasterService masterService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getSmartIdLoginPage() {
        masterService.saveUserStatistics(request, "/login");
        return "login/index";
    }
}
