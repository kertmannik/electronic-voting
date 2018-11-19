package com.web_application_development.evoting.smartid;

import ee.sk.smartid.AuthenticationHash;
import ee.sk.smartid.SmartIdAuthenticationResult;
import ee.sk.smartid.exception.DocumentUnusableException;
import ee.sk.smartid.exception.SmartIdException;
import ee.sk.smartid.exception.UserAccountNotFoundException;
import ee.sk.smartid.exception.UserRefusedException;
import ee.sk.smartid.rest.dao.NationalIdentity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(value = "/smart-id/authentication")
public class SmartIdController {

    private static final Logger logger = LogManager.getLogger(SmartIdController.class);

    public static final String AUTHENTICATION_RESULT_KEY = "SMART_ID_AUTHENTICATION_RESULT";
    private final SmartIdService smartIdService;
    private final MessageSource messageSource;

    @Autowired
    SmartIdController(SmartIdService smartIdService, MessageSource messageSource) {
        this.smartIdService = smartIdService;
        this.messageSource = messageSource;
    }
    @PostMapping(value = "/start")
    public Verification startAuthentication(@RequestBody NationalIdentity nationalIdentity, HttpSession httpSession) {
        // For security reasons a new hash value must be created for each new authentication request
        AuthenticationHash authenticationHash = smartIdService.getAuthenticationHash();
        httpSession.setAttribute("nationalIdentity", nationalIdentity);
        httpSession.setAttribute("authenticationHash", authenticationHash);
        Verification verification = new Verification();
        verification.setCode(authenticationHash.calculateVerificationCode());
        logger.debug("Smart-ID login started: nationalIdentity " + nationalIdentity + ", authenticationHash " + authenticationHash);
        return verification;
    }
    @PostMapping(value = "/poll")
    public SmartIdAuthenticationResult pollAuthenticationResult(HttpSession httpSession) {
        logger.debug("Smart-ID login poll");
        NationalIdentity nationalIdentity = (NationalIdentity) httpSession.getAttribute("nationalIdentity");
        AuthenticationHash authenticationHash = (AuthenticationHash) httpSession.getAttribute("authenticationHash");
        SmartIdAuthenticationResult authenticationResult = smartIdService.authenticate(nationalIdentity, authenticationHash);
        httpSession.setAttribute(AUTHENTICATION_RESULT_KEY, authenticationResult);
        return authenticationResult;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = SmartIdException.class)
    public Map<String, String> smartIdExceptionHandler(SmartIdException smartIdException) {
        Map<String, String> errorMap = new HashMap<>();
        if (smartIdException instanceof UserAccountNotFoundException) {
            logger.error("Smart-ID ERROR: " + messageSource.getMessage("error.smartidnotfound", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidnotfound", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else if (smartIdException instanceof UserRefusedException) {
            logger.error("Smart-ID ERROR: " + messageSource.getMessage("error.smartidrefused", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidrefused", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else if (smartIdException instanceof DocumentUnusableException) {
            logger.error("Smart-ID ERROR: " + messageSource.getMessage("error.smartidunusable", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidunusable", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        } else {
            logger.error("Smart-ID ERROR: " + messageSource.getMessage("error.smartidother", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
            errorMap.put("errorMessage", messageSource.getMessage("error.smartidother", Collections.emptyList().toArray(), LocaleContextHolder.getLocale()));
        }
        return errorMap;
    }
}

