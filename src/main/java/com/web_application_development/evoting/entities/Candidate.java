package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "candidates")
public class Candidate {

    @Id
    private long id;
    private String identityCode;
    private String firstName;
    private String lastName;
    private String region;
    private String party;
    private long hasWithdrawn;
    private java.sql.Timestamp candidacyAnnounced;

}
