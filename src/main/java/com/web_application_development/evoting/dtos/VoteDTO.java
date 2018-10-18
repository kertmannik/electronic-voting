package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VoteDTO {
    private Integer candidateId;

    public VoteDTO(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }
}
