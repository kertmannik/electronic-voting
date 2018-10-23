package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateForVotingDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.MasterService;
import com.web_application_development.evoting.services.UserStatisticsService;
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


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MasterService masterService;

    @Autowired
    private UserStatisticsService userStatisticsService;

    HomeController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/")
    public String showAllVotes(Model model) {
        userStatisticsService.saveUserStatistics(request, "/");
        List<Object[]> candidateListObj = masterService.findAllCandidates();
        List<CandidateForVotingDTO> candidateList = new ArrayList<>();
        for (Object[] candidate : candidateListObj) {
            candidateList.add(new CandidateForVotingDTO((Integer) candidate[0],
                    (String) candidate[1],
                    (String) candidate[2],
                    (String) candidate[3],
                    (String) candidate[4]));
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
        masterService.saveVote(voteDTO, authIdentity.getIdentityCode());

        Candidate candidate = masterService.findCandidateById(voteDTO.getCandidateId());
        messagingTemplate.convertAndSend("/topic/votes", candidate);

        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }
}
