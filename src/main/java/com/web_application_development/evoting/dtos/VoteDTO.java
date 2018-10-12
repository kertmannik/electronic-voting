package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VoteDTO {
    private String voterIdentityCode;
    private Integer candidateId;

    public VoteDTO(Integer candidateId) {
        this.voterIdentityCode = "12345678910";
        this.candidateId = candidateId;
    }

    public String getVoterIdentityCode() {
        return voterIdentityCode;
    }

    public void setVoterIdentityCode(String voterIdentityCode) {
        this.voterIdentityCode = voterIdentityCode;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }
}
