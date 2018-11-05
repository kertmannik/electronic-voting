package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteResultsRepository extends JpaRepository<VoteResult, Long> {

    @Query(value = "SELECT v.candidate_id as id, c.first_name as first_name, c.last_name as last_name, c.region as region, c.party as party, COUNT(v.candidate_id) as count " +
            "FROM votes v " +
            "JOIN candidates c ON v.candidate_id = c.id " +
            "WHERE v.is_withdrawn = 0 and c.has_withdrawn = 0 " +
            "GROUP BY v.candidate_id, c.first_name, c.last_name, c.region, c.party ORDER BY COUNT(v.candidate_id) DESC",
            nativeQuery = true)
    List<VoteResult> findAllVotes();
}
