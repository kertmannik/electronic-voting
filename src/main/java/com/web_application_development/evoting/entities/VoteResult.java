package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Entity
@Getter
@Setter
public class VoteResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @NotNull
    private String firstName;

    @Size(max = 50)
    @NotNull
    private String lastName;

    @Size(max = 50)
    @NotNull
    private String region;

    @Size(max = 50)
    @NotNull
    private String party;

    private BigInteger count;
}
