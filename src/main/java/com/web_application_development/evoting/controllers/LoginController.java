package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.services.UserStatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);


    private final HttpServletRequest request;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    public LoginController(HttpServletRequest request, UserStatisticsService userStatisticsService) {
        this.request = request;
        this.userStatisticsService = userStatisticsService;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getSmartIdLoginPage() {
        logger.debug("Login page GET request");
        userStatisticsService.saveUserStatistics(request, "/login");
        return "login/index";
    }
}
