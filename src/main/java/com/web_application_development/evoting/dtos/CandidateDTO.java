package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {
    private String identityCode;
    private String firstName;
    private String lastName;
    private String region;
    private String party;
}
