package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.CandidateService;
import com.web_application_development.evoting.services.VoteService;
import ee.sk.smartid.AuthenticationIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/voting")
public class VoteController {

    private static final Logger logger = LogManager.getLogger(VoteController.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final VoteService voteService;
    private final CandidateService candidateService;

    @Autowired
    VoteController(SimpMessageSendingOperations messagingTemplate, VoteService voteService, CandidateService candidateService) {
        this.messagingTemplate = messagingTemplate;
        this.voteService = voteService;
        this.candidateService = candidateService;
    }

    @PostMapping(value = "/vote")
    public void sendVote(@RequestBody VoteDTO voteDTO) throws Exception {
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());

        Candidate candidate = candidateService.findCandidateById(voteDTO.getCandidateId());
        if (isCandidateSameAsUser(authIdentity, candidate)) {
            logger.error("User " + authIdentity.getIdentityCode() + " tried to vote for themself -> ERROR");
            throw new Exception("Can not vote for yourself");
        } else {
            voteService.saveVote(voteDTO, authIdentity.getIdentityCode());
            messagingTemplate.convertAndSend("/topic/votes", candidate);
            logger.debug("Send vote POST request: to " + candidate.getIdentityCode() + " from " + authIdentity.getIdentityCode());
        }
    }

    private boolean isCandidateSameAsUser(AuthenticationIdentity authIdentity, Candidate candidate) {
        return authIdentity.getIdentityCode().equals(candidate.getIdentityCode());
    }
}
