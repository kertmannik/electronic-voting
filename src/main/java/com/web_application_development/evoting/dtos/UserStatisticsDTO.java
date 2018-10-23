package com.web_application_development.evoting.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatisticsDTO {
    private String sessionId;
    private String landingPage;
    private String browser;
    private String ip;
}
