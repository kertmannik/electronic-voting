package com.web_application_development.evoting.smartid;

import ee.sk.smartid.*;
import ee.sk.smartid.rest.dao.NationalIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SmartIdService {
    private final SmartIdClient smartIdClient;
    private final AuthenticationResponseValidator authenticationResponseValidator;
    @Autowired
    SmartIdService(SmartIdClient smartIdClient, AuthenticationResponseValidator authenticationResponseValidator) {
        this.smartIdClient = smartIdClient;
        this.authenticationResponseValidator = authenticationResponseValidator;
    }
    public AuthenticationHash getAuthenticationHash() {
        return AuthenticationHash.generateRandomHash();
    }
    public SmartIdAuthenticationResult authenticate(NationalIdentity nationalIdentity, AuthenticationHash authenticationHash) {
        SmartIdAuthenticationResponse authenticationResponse = smartIdClient
                .createAuthentication()
                .withNationalIdentity(nationalIdentity)
                .withAuthenticationHash(authenticationHash)
                .withCertificateLevel("QUALIFIED") // Certificate level can either be "QUALIFIED" or "ADVANCED"
                .withDisplayText("Sisselogimine")
                .authenticate();
        return authenticationResponseValidator.validate(authenticationResponse);
    }
}