package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "select id, first_name, last_name, region, party from candidates where has_withdrawn = 0",
            nativeQuery = true)
    List<Object[]> findAllCandidates();
}
