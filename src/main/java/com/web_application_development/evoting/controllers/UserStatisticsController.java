package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.services.UserStatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserStatisticsController {

    private static final Logger logger = LogManager.getLogger(UserStatisticsController.class);

    private final HttpServletRequest request;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    public UserStatisticsController(HttpServletRequest request, UserStatisticsService userStatisticsService) {
        this.request = request;
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping("/userstatistics")
    public String showStatistics(Model model) {
        logger.debug("User statistics page GET request");
        userStatisticsService.saveUserStatistics(request, "/userstatistics");
        model.addAttribute("visitors", userStatisticsService.getUniqueVisitorsToday());
        model.addAttribute("browsers", userStatisticsService.getTopBrowsers());
        model.addAttribute("landings", userStatisticsService.getTopLandingPages());
        return "userstatistics/index";
    }
}
