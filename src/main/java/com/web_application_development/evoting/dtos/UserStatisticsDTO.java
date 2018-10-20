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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UserStatisticsDTO(String sessionId, String landingPage, String browser, String ip) {
        this.sessionId = sessionId;
        this.landingPage = landingPage;
        this.browser = browser;
        this.ip = ip;
    }
}
