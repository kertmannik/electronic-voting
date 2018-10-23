package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
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
}
