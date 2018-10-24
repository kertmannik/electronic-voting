package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "SELECT id, identity_code, first_name, last_name, region, party, has_withdrawn, candidacy_announced FROM candidates WHERE has_withdrawn = 0 ORDER BY id",
            nativeQuery = true)
    List<Candidate> findAllCandidates();

    @Override
    Optional<Candidate> findById(Long id);
}
