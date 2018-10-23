package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Vote;
import com.web_application_development.evoting.entities.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = "select v.candidate_id, c.first_name, c.last_name, c.region, c.party, count(v.candidate_id) " +
            "from votes v " +
            "join candidates c on v.candidate_id = c.id " +
            "where v.is_withdrawn = 0 and c.has_withdrawn = 0" +
            "group by v.candidate_id, c.first_name, c.last_name, c.region, c.party ORDER BY count(v.candidate_id) DESC",
            nativeQuery = true)
    List<VoteResult[]> findAllVotes();
}
