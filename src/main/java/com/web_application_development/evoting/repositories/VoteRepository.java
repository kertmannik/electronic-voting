package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM votes " +
            "WHERE voter_identity_code = :idenCode AND is_withdrawn = 0", nativeQuery = true)
    boolean hasVoted(@Param("idenCode") String idenCode);

    @Modifying
    @Query(value = "UPDATE votes SET is_withdrawn = 1 WHERE voter_identity_code = :idenCode AND is_withdrawn = 0;", nativeQuery = true)
    void takeBackCandidacy(@Param("idenCode") String idenCode);
}
