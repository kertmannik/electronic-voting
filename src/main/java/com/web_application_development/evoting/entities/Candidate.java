package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
