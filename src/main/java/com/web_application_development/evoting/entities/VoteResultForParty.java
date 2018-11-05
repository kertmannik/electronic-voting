package com.web_application_development.evoting.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Entity
@Getter
@Setter
public class VoteResultForParty {

    @Id
    @Size(max = 50)
    @NotNull
    private String party;

    private BigInteger count;
}
