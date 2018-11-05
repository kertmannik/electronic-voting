package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.VoteResultForParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteResultsForPartyRepository extends JpaRepository<VoteResultForParty, Long> {

    @Query(value = "SELECT c.party as party, COUNT(c.party) as count\n" +
            "FROM votes v \n" +
            "JOIN candidates c ON v.candidate_id = c.id \n" +
            "WHERE v.is_withdrawn = 0 and c.has_withdrawn = 0 \n" +
            "GROUP BY c.party ORDER BY COUNT(c.party) DESC",
            nativeQuery = true)
    List<VoteResultForParty> findAllVotesForEachParty();
}
