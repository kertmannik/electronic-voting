package com.web_application_development.evoting.services;

import com.web_application_development.evoting.dtos.VoteDTO;
import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public List<Object[]> findAllVotes() {
        return voteRepository.findAllVotes();
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
}
