package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.services.CandidateService;
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

    private final HttpServletRequest request;
    private final CandidateService candidateService;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    public CandidacyController(HttpServletRequest request, CandidateService candidateService, UserStatisticsService userStatisticsService) {
        this.request = request;
        this.candidateService = candidateService;
        this.userStatisticsService = userStatisticsService;
    }

    @RequestMapping(path = "/candidacy", method = RequestMethod.GET)
    public String getTestPage() {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        return "candidacy/index";
    }

    @PostMapping(path = "/candidacy")
    public String sendSubscription(@ModelAttribute CandidateDTO candidateDTO) {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());

        candidateService.saveCandidate(candidateDTO, authIdentity);

        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }

}
