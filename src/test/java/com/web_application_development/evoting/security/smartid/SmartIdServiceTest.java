package com.web_application_development.evoting.security.smartid;

import com.web_application_development.evoting.smartid.SmartIdService;
import ee.sk.smartid.*;
import ee.sk.smartid.rest.dao.NationalIdentity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class SmartIdServiceTest {
    @Mock
    private AuthenticationResponseValidator authenticationResponseValidator;
    @Mock
    private AuthenticationRequestBuilder mockRequestBuilder;
    @Mock
    private SmartIdClient smartIdClient;
    @InjectMocks
    private SmartIdService smartIdService;
    private NationalIdentity nationalIdentity;
    private AuthenticationHash authenticationHash;
    @Before
    public void setUpSIDServiceTest() {
        nationalIdentity = new NationalIdentity();
        nationalIdentity.setCountryCode("EE");
        nationalIdentity.setNationalIdentityNumber("12345678910");
        authenticationHash = smartIdService.getAuthenticationHash();
    }
    @Test
    public void testGetAuthenticationHash() {
        AuthenticationHash authenticationHash = smartIdService.getAuthenticationHash();
        assertEquals(HashType.SHA512, authenticationHash.getHashType());
        assertTrue(authenticationHash.getHashInBase64().length() > 0);
    }
    @Test
    public void smartIdResponseIsValidatedAgainstAuthenticationValidator() {
        SmartIdAuthenticationResponse smartIdResponse = new SmartIdAuthenticationResponse();
        mockSmartIdResponse(smartIdResponse);
        smartIdService.authenticate(nationalIdentity, authenticationHash);
        verify(authenticationResponseValidator).validate(smartIdResponse);
    }
    @Test
    public void verifySmartIdRequest() {
        mockSmartIdResponse();
        smartIdService.authenticate(nationalIdentity, authenticationHash);
        verify(mockRequestBuilder).withNationalIdentity(nationalIdentity);
        verify(mockRequestBuilder).withDisplayText(eq("Sisselogimine"));
        verify(mockRequestBuilder).withCertificateLevel(eq("QUALIFIED"));
        verify(mockRequestBuilder).withAuthenticationHash(authenticationHash);
        verify(mockRequestBuilder).authenticate();
    }
    @Test
    public void testSuccessfulAuthentication() {
        // Initialize Smart-ID mocks
        mockSmartIdResponse();
        SmartIdAuthenticationResult mockedAuthenticationResult = new SmartIdAuthenticationResult();
        when(authenticationResponseValidator.validate(any())).thenReturn(mockedAuthenticationResult);
        SmartIdAuthenticationResult authenticationResult = smartIdService.authenticate(nationalIdentity, authenticationHash);
        assertEquals(mockedAuthenticationResult, authenticationResult);
        assertTrue(authenticationResult.isValid());
        assertEquals(0, authenticationResult.getErrors().size());
    }
    private void mockSmartIdResponse(SmartIdAuthenticationResponse smartIdResponse) {
        when(smartIdClient.createAuthentication()).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.withNationalIdentity(any())).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.withAuthenticationHash(any())).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.withCertificateLevel(anyString())).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.withDisplayText(anyString())).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.authenticate()).thenReturn(smartIdResponse);
    }
    private void mockSmartIdResponse() {
        mockSmartIdResponse(new SmartIdAuthenticationResponse());
    }
}