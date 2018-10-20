package com.web_application_development.evoting.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "user_statistics")
public class UserStatistics {

    public UserStatistics(@Size(max = 50) @NotNull String sessionId, @Size(max = 50) @NotNull String landingPage, @Size(max = 50) @NotNull String browser, @Size(max = 50) @NotNull String ip, Timestamp timestamp) {
        this.sessionId = sessionId;
        this.landingPage = landingPage;
        this.browser = browser;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    private java.sql.Timestamp timestamp;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


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


    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}