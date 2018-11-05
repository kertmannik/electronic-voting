package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {
    private String identityCode;
    private String firstName;
    private String lastName;
    private String region;
    private String party;

    public CandidateDTO(String identityCode, String firstName, String lastName, String region, String party) {
        this.identityCode = identityCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.region = region;
        this.party = party;
    }
}
