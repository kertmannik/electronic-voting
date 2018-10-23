package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotNull
    private String sessionId;

    @Size(max = 50)
    @NotNull
    private String landingPage;

    @Size(max = 50)
    @NotNull
    private String browser;

    @Size(max = 50)
    @NotNull
    private String ip;

    @NotNull
    private LocalDate timestamp;
}
