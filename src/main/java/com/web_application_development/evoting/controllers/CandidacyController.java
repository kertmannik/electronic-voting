package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.services.MasterService;
import com.web_application_development.evoting.services.UserStatisticsService;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CandidacyController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MasterService masterService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    @RequestMapping(path = "/candidacy", method = RequestMethod.GET)
    public String getTestPage() {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        return "candidacy/index";
    }

    @PostMapping(path = "/candidacy")
    public String sendSubscription(@ModelAttribute CandidateDTO candidateDTO) {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());

        masterService.saveCandidate(candidateDTO, authIdentity);

        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }

}
