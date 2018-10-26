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
    public String getPage(Model model) {
        userStatisticsService.saveUserStatistics(request, "/candidacy");
        setCandidateFeedbacks(model);
        return "candidacy/index";
    }

    @PostMapping(path = "/candidacy")
    public String sendSubscription(@ModelAttribute CandidateDTO candidateDTO, Model model) {
        try {
            userStatisticsService.saveUserStatistics(request, "/candidacy");
            AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
            candidateService.saveCandidate(candidateDTO, authIdentity);
            model.addAttribute("candidacySuccessMessage", messageSource.getMessage("error.candidacysuccess", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } catch (Exception exception) {
            model.addAttribute("candidacyErrorMessage", messageSource.getMessage("error.candidacyerror", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        setCandidateFeedbacks(model);
        return "candidacy/index";
    }

    @PostMapping(path = "/take_back_candidacy")
    public String takeBackCandidacy(Model model) {
        try {
            userStatisticsService.saveUserStatistics(request, "/candidacy");
            AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
            candidateService.takeBackCandidacy(authIdentity.getIdentityCode());
            model.addAttribute("candidacySuccessTakeBack", messageSource.getMessage("error.candidacytakebacksuccess", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } catch (Exception exception) {
            model.addAttribute("candidacyErrorTakeBack", messageSource.getMessage("error.candidacytakebackerror", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        setCandidateFeedbacks(model);
        return "candidacy/index";
    }

    private boolean isCandidate() {
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        return candidateService.isCandidate(authIdentity.getIdentityCode());
    }

    private void setCandidateFeedbacks(Model model) {
        if (isCandidate()) {
            model.addAttribute("iscandidate", true);
        } else {
            model.addAttribute("iscandidate", false);
        }
    }
}
