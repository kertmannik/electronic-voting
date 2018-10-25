package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.services.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private final HttpServletRequest request;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    public LoginController(HttpServletRequest request, UserStatisticsService userStatisticsService) {
        this.request = request;
        this.userStatisticsService = userStatisticsService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getSmartIdLoginPage() {
        userStatisticsService.saveUserStatistics(request, "/login");
        return "login/index";
    }
}
