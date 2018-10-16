package com.web_application_development.evoting.controllers;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.repositories.CandidateRepository;
import ee.sk.smartid.AuthenticationIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;

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
        AuthenticationIdentity authIdentity = ((AuthenticationIdentity) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal());

        // map DTO to entity
        Candidate entity = mapDtoToEntity(candidateDTO, authIdentity);
        // save new entity
        candidateRepository.save(entity);
        // redirect to home page where all candidates are displayed
        return "redirect:/";
    }

    private Candidate mapDtoToEntity(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        Candidate candidateEntity = new Candidate();
        String givenName = authIdentity.getGivenName();
        String lastName = authIdentity.getSurName();
        candidateEntity.setFirstName(
                givenName.substring(0, 1).toUpperCase() + givenName.substring(1).toLowerCase());
        candidateEntity.setLastName(
                lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase());
        candidateEntity.setIdentityCode(authIdentity.getIdentityCode());
        candidateEntity.setRegion(candidateDTO.getRegion());
        candidateEntity.setParty(candidateDTO.getParty());
        candidateEntity.setHasWithdrawn(0);
        candidateEntity.setCandidacyAnnounced(new Timestamp(System.currentTimeMillis()));
        return candidateEntity;
    }
}
