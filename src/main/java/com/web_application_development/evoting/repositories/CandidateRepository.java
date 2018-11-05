package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "SELECT id, identity_code, first_name, last_name, region, party, has_withdrawn, candidacy_announced FROM candidates WHERE has_withdrawn = 0 ORDER BY id",
            nativeQuery = true)
    List<Candidate> findAllCandidates();

    @Override
    Optional<Candidate> findById(Long id);

    @Query(value = "select * from candidates where id = (select candidate_id from votes where voter_identity_code = :idenCode and is_withdrawn = 0)",
            nativeQuery = true)
    Optional<Candidate> findCandidateUserVotedFor(@Param("idenCode") String idenCode);
  
    @Query(value = "SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM candidates " +
            "WHERE identity_code = :idenCode AND has_withdrawn = 0", nativeQuery = true)
    boolean isCandidate(@Param("idenCode") String idenCode);

    @Modifying
    @Query(value = "UPDATE candidates SET has_withdrawn = 1 WHERE identity_code = :idenCode AND has_withdrawn = 0;", nativeQuery = true)
    void takeBackCandidacy(@Param("idenCode") String idenCode);
}
