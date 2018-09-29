package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "votes")
public class Vote {

    @Id
    private long id;
    private String voterIdentityCode;
    private long candidateId;
    private long isWithdrawn;
    private java.sql.Timestamp votingTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getVoterIdentityCode() {
        return voterIdentityCode;
    }

    public void setVoterIdentityCode(String voterIdentityCode) {
        this.voterIdentityCode = voterIdentityCode;
    }


    public long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(long candidateId) {
        this.candidateId = candidateId;
    }


    public long getIsWithdrawn() {
        return isWithdrawn;
    }

    public void setIsWithdrawn(long isWithdrawn) {
        this.isWithdrawn = isWithdrawn;
    }


    public java.sql.Timestamp getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(java.sql.Timestamp votingTime) {
        this.votingTime = votingTime;
    }

}
