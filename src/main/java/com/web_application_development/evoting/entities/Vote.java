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

}
