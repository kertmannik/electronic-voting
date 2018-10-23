package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 11, max = 11)
    @NotNull
    private String identityCode;

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

    @Where(clause = "has_withdrawn = '0'")
    @NotNull
    private Long hasWithdrawn;

    @NotNull
    private LocalTime candidacyAnnounced;
}
