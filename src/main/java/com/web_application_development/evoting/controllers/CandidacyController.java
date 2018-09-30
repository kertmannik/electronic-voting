package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CandidacyController {

    private final CandidateRepository candidateRepository;

    @Autowired
    CandidacyController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @RequestMapping(path = "/candidacy", method = RequestMethod.GET)
    public String getTestPage() {
        return "candidacy/index";
    }

    @PostMapping(path = "/candidacy")
    public String sendSubscription(@ModelAttribute CandidateDTO candidateDTO) {
        // map DTO to entity
        Candidate entity = mapDtoToEntity(candidateDTO);
        // save new entity
        candidateRepository.save(entity);
        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }

    private Candidate mapDtoToEntity(CandidateDTO candidateDTO) {
        Candidate candidateEntity = new Candidate();
        candidateEntity.setFirstName(candidateDTO.getFirstName());
        candidateEntity.setLastName(candidateDTO.getLastName());
        candidateEntity.setIdentityCode(candidateDTO.getIdentityCode());
        candidateEntity.setRegion(candidateDTO.getRegion());
        candidateEntity.setParty(candidateDTO.getParty());
        return candidateEntity;
    }
}
