package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.CandidateDTO;
import com.web_application_development.evoting.dtos.UserStatisticsDTO;
import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Candidate;
import com.web_application_development.evoting.entities.UserStatistics;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.repositories.CandidateRepository;
import com.web_application_development.evoting.repositories.UserStatisticsRepository;
import com.web_application_development.evoting.repositories.VoteRepository;
import ee.sk.smartid.AuthenticationIdentity;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class MasterService {

    @Autowired
    private final CandidateRepository candidateRepository;

    @Autowired
    private final VoteRepository voteRepository;

    MasterService(CandidateRepository candidateRepository, VoteRepository voteRepository, UserStatisticsRepository userStatisticsRepository) {
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
    }

    public List<Object[]> findAllCandidates() {
        return candidateRepository.findAllCandidates();
    }

    public List<Object[]> findAllVotes() {
        return voteRepository.findAllVotes();
    }

    public Candidate findCandidateById(Integer candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public void saveCandidate(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
        // map DTO to entity
        Candidate entity = mapDTOToEntity(candidateDTO, authIdentity);
        // save new entity
        candidateRepository.save(entity);
    }

    public void saveVote(VoteDTO voteDTO, String voterId) {
        Vote entity = mapDTOToEntity(voteDTO, voterId);
        voteRepository.save(entity);
    }

    private Vote mapDTOToEntity(VoteDTO voteDTO, String voterId) {
        Vote voteEntity = new Vote();
        voteEntity.setVoterIdentityCode(voterId);
        voteEntity.setCandidateId(voteDTO.getCandidateId());
        voteEntity.setIsWithdrawn(0);
        voteEntity.setVotingTime(new Timestamp(System.currentTimeMillis()));
        return voteEntity;
    }

    private Candidate mapDTOToEntity(CandidateDTO candidateDTO, AuthenticationIdentity authIdentity) {
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
