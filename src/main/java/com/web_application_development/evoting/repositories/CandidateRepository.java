package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query(value = "SELECT id, first_name, last_name, region, party FROM candidates WHERE has_withdrawn = 0 ORDER BY id",
            nativeQuery = true)
    List<Candidate[]> findAllCandidates();

//    @Query(value = "SELECT * FROM candidates WHERE id = :id AND has_withdrawn = 0",
//            nativeQuery = true)
//    Candidate findById(@Param("id") long id);

    @Override
    Optional<Candidate> findById(Long aLong);
}
