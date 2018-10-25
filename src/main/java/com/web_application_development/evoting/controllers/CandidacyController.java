package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.UserStatisticsService;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Controller
public class CandidacyController {

    private final HttpServletRequest request;
    private final CandidateService candidateService;
    private final UserStatisticsService userStatisticsService;
    private final MessageSource messageSource;

    @Autowired
    public CandidacyController(HttpServletRequest request, CandidateService candidateService, UserStatisticsService userStatisticsService, MessageSource messageSource) {
        this.request = request;
        this.candidateService = candidateService;
        this.userStatisticsService = userStatisticsService;
        this.messageSource = messageSource;
    }

    @GetMapping(path = "/candidacy")
    public String getTestPage() {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        return "candidacy/index";
    }

    @PostMapping(path = "/candidacy")
    public String sendSubscription(@ModelAttribute CandidateDTO candidateDTO, Model model) {
        try {
            userStatisticsService.saveUserStatistics(request, "/candidacy");
            AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
            candidateService.saveCandidate(candidateDTO, authIdentity);
            model.addAttribute("candidacySuccessMessage", messageSource.getMessage("candidacy.successmessage", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } catch (Exception exception) {
            model.addAttribute("candidacyErrorMessage", messageSource.getMessage("candidacy.successmessage", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        return "candidacy/index";
    }

}
