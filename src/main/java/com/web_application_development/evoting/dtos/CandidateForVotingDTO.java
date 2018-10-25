package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateForVotingDTO {
    private Integer candidateId;
    private String firstName;
    private String lastName;
    private String region;
    private String party;

    public CandidateForVotingDTO(Integer id, String firstName, String lastName, String region, String party) {
        this.candidateId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.region = region;
        this.party = party;
    }
}

