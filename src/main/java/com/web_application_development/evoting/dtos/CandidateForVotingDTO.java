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

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}

