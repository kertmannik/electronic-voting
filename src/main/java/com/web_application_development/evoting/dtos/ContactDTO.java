package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
    private String name;
    private String email;
    private String subject;
    private String body;
}
