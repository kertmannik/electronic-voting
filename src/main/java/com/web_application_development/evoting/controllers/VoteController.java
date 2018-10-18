package com.web_application_development.evoting.controllers;

import com.google.gson.Gson;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.services.MasterService;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping(value = "/voting")
public class VoteController {
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private MasterService masterService;

    VoteController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping(value = "/vote", produces = APPLICATION_JSON)
    public ResponseEntity<Void> sendVote(@RequestBody String candidateId) {
        VoteDTO voteDTO = fromJsonToJava(candidateId);
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());
        // save new entity
        masterService.saveVote(voteDTO, authIdentity.getIdentityCode());

        Candidate candidate = masterService.findCandidateById(voteDTO.getCandidateId());
        messagingTemplate.convertAndSend("/topic/votes", candidate);

        return ResponseEntity.ok().build();
    }

    private VoteDTO fromJsonToJava(String rawData) {
        final Gson gson = new Gson();
        return gson.fromJson(rawData, VoteDTO.class);
    }
}
