package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateForVotingDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.UserStatisticsService;
import com.web_application_development.evoting.services.VoteService;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final HttpServletRequest request;
    private final CandidateService candidateService;
    private final VoteService voteService;
    private final UserStatisticsService userStatisticsService;

    @Autowired
    HomeController(SimpMessageSendingOperations messagingTemplate, HttpServletRequest request, CandidateService candidateService, VoteService voteService, UserStatisticsService userStatisticsService) {
        this.messagingTemplate = messagingTemplate;
        this.request = request;
        this.candidateService = candidateService;
        this.voteService = voteService;
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping("/")
    public String showAllVotes(Model model) {
        userStatisticsService.saveUserStatistics(request, "/");
        List<Candidate> candidateListObj = candidateService.findAllCandidates();
        List<CandidateForVotingDTO> candidateList = new ArrayList<>();
        for (Candidate candidate : candidateListObj) {
            CandidateForVotingDTO candidateDTO = new CandidateForVotingDTO(candidate.getId().intValue(), candidate.getFirstName(), candidate.getLastName(), candidate.getRegion(), candidate.getParty());
            candidateList.add(candidateDTO);
        }
        model.addAttribute("candidatesForVoting", candidateList);
        return "home/index";
    }

    @Deprecated
    @PostMapping(path = "/add_vote")
    public String sendVote(@ModelAttribute VoteDTO voteDTO) {
        userStatisticsService.saveUserStatistics(request, "/add_vote");
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        // save new entity
        voteService.saveVote(voteDTO, authIdentity.getIdentityCode());

        Candidate candidate = candidateService.findCandidateById(voteDTO.getCandidateId());
        messagingTemplate.convertAndSend("/topic/votes", candidate);

        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }
}
