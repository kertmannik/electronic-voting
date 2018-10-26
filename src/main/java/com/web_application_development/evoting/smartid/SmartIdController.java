package com.web_application_development.evoting.smartid;

import ee.sk.smartid.*;
import ee.sk.smartid.exception.DocumentUnusableException;
import ee.sk.smartid.exception.SmartIdException;
import ee.sk.smartid.exception.UserAccountNotFoundException;
import ee.sk.smartid.exception.UserRefusedException;
import ee.sk.smartid.rest.dao.NationalIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(value = "/smart-id/authentication")
public class SmartIdController {

    private final SmartIdClient smartIdClient;
    private final MessageSource messageSource;

    @Autowired
    SmartIdController(SmartIdClient smartIdClient, MessageSource messageSource) {
        this.smartIdClient = smartIdClient;
        this.messageSource = messageSource;
    }
    @PostMapping(value = "/start")
    public Verification startAuthentication(@RequestBody NationalIdentity nationalIdentity, HttpSession httpSession) {
        // For security reasons a new hash value must be created for each new authentication request
        AuthenticationHash authenticationHash = AuthenticationHash.generateRandomHash();
        httpSession.setAttribute("nationalIdentity", nationalIdentity);
        httpSession.setAttribute("authenticationHash", authenticationHash);
        Verification verification = new Verification();
        verification.setCode(authenticationHash.calculateVerificationCode());
        return verification;
    }
    @PostMapping(value = "/poll")
    public SmartIdAuthenticationResult pollAuthenticationResult(HttpSession httpSession) {
        SmartIdAuthenticationResponse authenticationResponse = smartIdClient
                .createAuthentication()
                .withNationalIdentity((NationalIdentity) httpSession.getAttribute("nationalIdentity"))
                .withAuthenticationHash((AuthenticationHash) httpSession.getAttribute("authenticationHash"))
                .withCertificateLevel("QUALIFIED") // Certificate level can either be "QUALIFIED" or "ADVANCED"
                .withDisplayText(messageSource.getMessage("smartid.welcome", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()))
                .authenticate();
        AuthenticationResponseValidator authenticationResponseValidator = new AuthenticationResponseValidator();
        SmartIdAuthenticationResult authenticationResult = authenticationResponseValidator.validate(authenticationResponse);
        Authentication auth = new SmartIdAuthenticationToken(
                authenticationResult.getAuthenticationIdentity(),
                null,
                null
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return authenticationResult;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = SmartIdException.class)
    public Map<String, String> smartIdExceptionHandler(SmartIdException smartIdException) {
        Map<String, String> errorMap = new HashMap<>();
        if (smartIdException instanceof UserAccountNotFoundException) {
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidnotfound", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else if (smartIdException instanceof UserRefusedException) {
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidrefused", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else if (smartIdException instanceof DocumentUnusableException) {
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidunusable", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else {
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidother", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        return errorMap;
    }
}

