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

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
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
