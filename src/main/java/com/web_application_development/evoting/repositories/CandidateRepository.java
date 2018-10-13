package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "SELECT id, first_name, last_name, region, party FROM candidates WHERE has_withdrawn = 0 ORDER BY id",
            nativeQuery = true)
    List<Object[]> findAllCandidates();

    @Query(value = "SELECT * FROM candidates WHERE id = :id AND has_withdrawn = 0",
            nativeQuery = true)
    Candidate findByVoteId(@Param("id") long id);
}
