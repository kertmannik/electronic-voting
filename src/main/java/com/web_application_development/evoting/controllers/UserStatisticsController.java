package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.services.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    @Autowired
    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping("/userstatistics")
    public String showStatistics(Model model) {
        model.addAttribute("visitors", userStatisticsService.getUniqueVisitorsToday());
        model.addAttribute("browsers", userStatisticsService.getTopBrowsers());
        model.addAttribute("landings", userStatisticsService.getTopLandingPages());
        return "userstatistics/index";
    }
}
