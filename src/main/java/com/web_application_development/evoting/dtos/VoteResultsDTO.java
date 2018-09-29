package com.web_application_development.evoting.dtos;

import java.math.BigInteger;

public class VoteResultsDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String region;
    private String party;
    private BigInteger cnt;

    public VoteResultsDTO(Integer id, String firstName, String lastName, String region, String party, BigInteger cnt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.region = region;
        this.party = party;
        this.cnt = cnt;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegion() {
        return region;
    }

    public String getParty() {
        return party;
    }

    public BigInteger getCnt() {
        return cnt;
    }
}
