package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.entities.VoteResult;
import com.web_application_development.evoting.repositories.VoteRepository;
import com.web_application_development.evoting.repositories.VoteResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteResultsRepository voteResultsRepository;

    @Autowired
    VoteService(VoteRepository voteRepository, VoteResultsRepository voteResultsRepository) {
        this.voteRepository = voteRepository;
        this.voteResultsRepository = voteResultsRepository;
    }

    public List<VoteResult> findAllVotes() {
        return voteResultsRepository.findAllVotes();
    }

    public void saveVote(VoteDTO voteDTO, String voterId) {
        Vote entity = mapDTOToEntity(voteDTO, voterId);
        voteRepository.save(entity);
    }

    private Vote mapDTOToEntity(VoteDTO voteDTO, String voterId) {
        Vote voteEntity = new Vote();
        voteEntity.setVoterIdentityCode(voterId);
        voteEntity.setCandidateId(voteDTO.getCandidateId().longValue());
        voteEntity.setIsWithdrawn(0L);
        voteEntity.setVotingTime(LocalDateTime.now());
        return voteEntity;
    }
}
