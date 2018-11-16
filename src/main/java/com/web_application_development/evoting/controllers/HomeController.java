package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateForVotingDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.UserStatisticsService;
import com.web_application_development.evoting.services.VoteService;
import ee.sk.smartid.AuthenticationIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final HttpServletRequest request;
    private final CandidateService candidateService;
    private final VoteService voteService;
    private final UserStatisticsService userStatisticsService;
    private final MessageSource messageSource;

    @Autowired
    HomeController(SimpMessageSendingOperations messagingTemplate, HttpServletRequest request, CandidateService candidateService, VoteService voteService, UserStatisticsService userStatisticsService, MessageSource messageSource) {
        this.messagingTemplate = messagingTemplate;
        this.request = request;
        this.candidateService = candidateService;
        this.voteService = voteService;
        this.userStatisticsService = userStatisticsService;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    public String showAllVotes(Model model) {
        logger.debug("Home page GET request");
        userStatisticsService.saveUserStatistics(request, "/");
        hasVotedSelect(model);
        model.addAttribute("candidatesForVoting", createCandidateTableList());
        model.addAttribute("selectedLanguage", LocaleContextHolder.getLocale());
        return "home/index";
    }

    @Deprecated
    @PostMapping(path = "/add_vote")
    public String sendVote(@ModelAttribute VoteDTO voteDTO, Model model) {
        userStatisticsService.saveUserStatistics(request, "/add_vote");
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());

        // save new entity
        voteService.saveVote(voteDTO, authIdentity.getIdentityCode());

        Candidate candidate = candidateService.findCandidateById(voteDTO.getCandidateId());
        logger.debug("Send vote POST request: to " + candidate.getIdentityCode() + " from " + authIdentity.getIdentityCode());
        messagingTemplate.convertAndSend("/topic/votes", candidate);
        hasVotedSelect(model);
        return "home/index";
    }

    @PostMapping(value = "/remove_vote")
    public String removeVote(Model model) {
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        try {
            userStatisticsService.saveUserStatistics(request, "/candidacy");
            voteService.takeBackVote(authIdentity.getIdentityCode());
            model.addAttribute("voteTakeBackSuccess", messageSource.getMessage("error.votetakebacksuccess", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
            logger.debug("Remove vote POST request SUCCESSFUL: take vote back from " + authIdentity.getIdentityCode());
        } catch (Exception exception) {
            logger.error("Remove vote POST request ERROR: take vote back from " + authIdentity.getIdentityCode());
            model.addAttribute("voteTakeBackError", messageSource.getMessage("error.votetakebackerror", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        hasVotedSelect(model);
        model.addAttribute("candidatesForVoting", createCandidateTableList());
        return "redirect:/";
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }

    private List<CandidateForVotingDTO> createCandidateTableList() {
        List<Candidate> candidateListObj = candidateService.findAllCandidates();
        List<CandidateForVotingDTO> candidateList = new ArrayList<>();
        for (Candidate candidate : candidateListObj) {
            CandidateForVotingDTO candidateDTO = mapEntityToDTO(candidate);
            candidateList.add(candidateDTO);
        }
        return candidateList;
    }

    private CandidateForVotingDTO mapEntityToDTO(Candidate candidate) {
        return new CandidateForVotingDTO(candidate.getId().intValue(), candidate.getFirstName(), candidate.getLastName(), candidate.getRegion(), candidate.getParty());
    }

    private void hasVotedSelect(Model model) {
        if (isAuthenticated()) {
            AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
            if (hasVoted()) {
                model.addAttribute("hasvoted", true);
                model.addAttribute("votedcandidate", candidateService.findCandidateUserVotedFor(authIdentity.getIdentityCode()));
            } else {
                model.addAttribute("hasvoted", false);
                model.addAttribute("votedcandidate", "");
            }
        }
    }

    private boolean hasVoted() {
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        return voteService.hasVoted(authIdentity.getIdentityCode());
    }
}
