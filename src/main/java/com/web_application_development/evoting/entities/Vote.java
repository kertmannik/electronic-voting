package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
    private LocalDate votingTime;
}
