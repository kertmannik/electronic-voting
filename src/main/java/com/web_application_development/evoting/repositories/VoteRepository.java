package com.web_application_development.evoting.repositories;

import com.web_application_development.evoting.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
