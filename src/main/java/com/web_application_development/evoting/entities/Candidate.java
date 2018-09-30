package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    private String identityCode;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String region;
    @NotNull
    private String party;
    @NotNull
    private long hasWithdrawn;
    @NotNull
    private java.sql.Timestamp candidacyAnnounced;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getHasWithdrawn() {
        return hasWithdrawn;
    }

    public void setHasWithdrawn(long hasWithdrawn) {
        this.hasWithdrawn = hasWithdrawn;
    }

    public Timestamp getCandidacyAnnounced() {
        return candidacyAnnounced;
    }

    public void setCandidacyAnnounced(Timestamp candidacyAnnounced) {
        this.candidacyAnnounced = candidacyAnnounced;
    }
}
