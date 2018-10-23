package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 11, max = 11)
    @NotNull
    private String voterIdentityCode;

    @NotNull
    private Long candidateId;

    @Where(clause = "is_withdrawn = '0'")
    @NotNull
    private Long isWithdrawn;

    @NotNull
    private java.sql.Timestamp votingTime;

    public Long getId() {
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

    public Timestamp getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(Timestamp votingTime) {
        this.votingTime = votingTime;
    }
}
