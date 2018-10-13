package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateForVotingDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.repositories.CandidateRepository;
import com.web_application_development.evoting.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final CandidateRepository candidateRepository;
    private final VoteRepository voteRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    HomeController(CandidateRepository candidateRepository, VoteRepository voteRepository, SimpMessageSendingOperations messagingTemplate) {
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/")
    public String showAllVotes(Model model) {
        List<Object[]> candidateListObj = candidateRepository.findAllCandidates();
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

    @PostMapping(path = "/add_vote")
    public String sendVote(@ModelAttribute VoteDTO voteDTO) {
        // map DTO to entity
        Vote entity = mapDtoToEntity(voteDTO);
        // save new entity
        voteRepository.save(entity);

        Long id = new Long(voteDTO.getCandidateId());
        Candidate candidate = candidateRepository.findByVoteId(id);
        messagingTemplate.convertAndSend("/topic/votes", candidate);

        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }

    private Vote mapDtoToEntity(VoteDTO voteDTO) {
        Vote voteEntity = new Vote();
        voteEntity.setVoterIdentityCode(voteDTO.getVoterIdentityCode());
        voteEntity.setCandidateId(voteDTO.getCandidateId());
        voteEntity.setIsWithdrawn(0);
        voteEntity.setVotingTime(new Timestamp(System.currentTimeMillis()));
        return voteEntity;
    }
}
