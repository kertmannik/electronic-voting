package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "votes")
public class Vote {

    @Id
    private long id;


    @Size(min = 11, max = 11)
    @NotNull
    private String voterIdentityCode;

    @NotNull
    private long candidateId;

    @NotNull
    private long isWithdrawn;

    @NotNull
    private java.sql.Timestamp votingTime;
}
